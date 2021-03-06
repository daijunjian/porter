/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.task.extract;

import cn.vbill.middleware.porter.common.statistics.NodeLog;
import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.core.event.etl.ETLBucket;
import cn.vbill.middleware.porter.core.task.StageType;
import cn.vbill.middleware.porter.task.extract.extractor.ExtractorFactory;
import cn.vbill.middleware.porter.core.event.s.MessageEvent;
import cn.vbill.middleware.porter.core.task.AbstractStageJob;
import cn.vbill.middleware.porter.datacarrier.DataCarrier;
import cn.vbill.middleware.porter.datacarrier.DataCarrierFactory;
import cn.vbill.middleware.porter.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 完成事件的进一步转换、过滤。多线程执行
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:20
 */
public class ExtractJob extends AbstractStageJob {
    //工作线程数量
    private static final int JOB_THREAD_SIZE = 2;

    private static final int BUFFER_SIZE = JOB_THREAD_SIZE * 10;
    private final TaskWork work;
    private final ExecutorService executorService;
    private final DataCarrier<ETLBucket> carrier;
    private final DataCarrier<String> orderedBucket;
    private final ExtractorFactory extractorFactory;
    private final ExtractMetadata metadata;
    public ExtractJob(TaskWork work) {
        super(work.getBasicThreadName(), 50L);
        extractorFactory = NodeContext.INSTANCE.getBean(ExtractorFactory.class);
        this.work = work;
        metadata = new ExtractMetadata(work.getDataConsumer().getExcludes(), work.getDataConsumer().getIncludes(),
                work.getDataConsumer().getEventProcessor());
        //线程阻塞时，在调用者线程中执行
        executorService = new ThreadPoolExecutor(JOB_THREAD_SIZE, JOB_THREAD_SIZE * 3,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(JOB_THREAD_SIZE * 5),
                getThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        carrier = NodeContext.INSTANCE.getBean(DataCarrierFactory.class).newDataCarrier(BUFFER_SIZE, 1);
        orderedBucket = NodeContext.INSTANCE.getBean(DataCarrierFactory.class).newDataCarrier(BUFFER_SIZE, 1);
    }

    @Override
    protected void doStop() {
        executorService.shutdown();
    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void loopLogic() throws InterruptedException {
        //只要队列有消息，持续读取
        Pair<String, List<MessageEvent>> events = null;
        do {
            try {
                events = work.waitEvent(StageType.SELECT);
                if (null != events) {
                    final Pair<String, List<MessageEvent>> inThreadEvents = events;
                    LOGGER.debug("extract MessageEvent batch {}.", inThreadEvents.getLeft());
                    //在单线程执行，保证将来DataLoader load顺序
                    orderedBucket.push(inThreadEvents.getLeft());
                    //暂无Extractor失败处理方案
                    executorService.submit(() -> {
                        try {
                            //将MessageEvent转换为ETLBucket
                            ETLBucket bucket = ETLBucket.from(inThreadEvents);
                            extractorFactory.extract(bucket, metadata);
                            carrier.push(bucket);
                            LOGGER.debug("push bucket {} into carrier after extract.", inThreadEvents.getLeft());
                        } catch (Throwable e) {
                            work.stopAndAlarm(e.getMessage());
                            LOGGER.error("批次[{}]执行ExtractJob失败!", inThreadEvents.getLeft(), e);
                        }
                    });
                }
            } catch (InterruptedException interrupt) {
                throw interrupt;
            } catch (Throwable e) {
                e.printStackTrace();
                NodeLog.upload(NodeLog.LogType.TASK_LOG, work.getTaskId(),  work.getDataConsumer().getSwimlaneId(),
                        "extract MessageEvent error" + e.getMessage());
                LOGGER.error("extract MessageEvent error!", e);
            }
        } while (null != events);
    }

    @Override
    public ETLBucket output() {
        return carrier.pull();
    }
    public <T> T getNextSequence() {
        return orderedBucket.pull();
    }

    @Override
    public boolean isPoolEmpty() {
        return orderedBucket.size() == 0 && carrier.size() == 0 && executorService.isTerminated();
    }

    @Override
    public boolean isPrevPoolEmpty() {
        return work.isPoolEmpty(StageType.SELECT);
    }

    @Override
    public boolean stopWaiting() {
        return work.getDataConsumer().isAutoCommitPosition();
    }
}
