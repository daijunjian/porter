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

package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.entity.MrNodesMonitor;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点任务实时监控表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrNodesMonitorMapper {

    /**
     * 新增
     *
     * @param mrNodesMonitor
     */
    Integer insert(MrNodesMonitor mrNodesMonitor);

    /**
     * 修改
     *
     * @param mrNodesMonitor
     */
    Integer update(@Param("id") Long id, @Param("mrNodesMonitor") MrNodesMonitor mrNodesMonitor);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    MrNodesMonitor selectById(Long id);

    MrNodesMonitor selectByNodeIdAndTime(@Param("nodeId") String nodeId, @Param("dataTimes") String dataTimes);

    /**
     * 分頁
     *
     * @return
     */
    List<MrNodesMonitor> page(@Param("page") Page<MrNodesMonitor> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * @param nodeId
     * @param startRow
     * @param intervalTime
     * @return
     */
    List<MrNodesMonitor> selectByNodeId(@Param("nodeId") String nodeId,
                                        @Param("startRow") Long startRow,
                                        @Param("intervalTime") Long intervalTime);

}