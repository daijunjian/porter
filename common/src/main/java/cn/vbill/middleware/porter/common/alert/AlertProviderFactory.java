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

package cn.vbill.middleware.porter.common.alert;

import cn.vbill.middleware.porter.common.alert.provider.NormalAlertProvider;
import cn.vbill.middleware.porter.common.client.AlertClient;
import cn.vbill.middleware.porter.common.client.impl.EmailClient;
import cn.vbill.middleware.porter.common.config.AlertConfig;
import cn.vbill.middleware.porter.common.dic.AlertPlugin;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.alert.provider.AlertProvider;
import cn.vbill.middleware.porter.common.config.source.EmailConfig;
import cn.vbill.middleware.porter.common.exception.ClientConnectionException;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 20:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月01日 20:14
 */
public enum AlertProviderFactory {
    INSTANCE();
    private final ReadWriteLock initializedLock = new ReentrantReadWriteLock();
    private AlertProvider alert;
    public void initialize(AlertConfig config) throws ConfigParseException, ClientConnectionException {
        //校验配置文件参数
        if (null == config || null == config.getStrategy() || null == config.getClient()
                || config.getClient().isEmpty()) {
            return;
        }

        initializedLock.writeLock().lock();
        try {
            if (config.getStrategy() == AlertPlugin.EMAIL) {
                AlertClient client = new EmailClient(new EmailConfig(config.getClient()).stuff(), config.getReceiver(),
                        config.getFrequencyOfSeconds());
                alert = new NormalAlertProvider(client);
            }
        } finally {
            initializedLock.writeLock().unlock();
        }
    }

    public void notice(String title, String msg, List<AlertReceiver> receiverList) {
        initializedLock.readLock().lock();
        if (null != alert) alert.notice(title, msg, receiverList);
        initializedLock.readLock().unlock();
    }
}
