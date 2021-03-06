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

import java.util.List;

import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import org.apache.ibatis.annotations.Param;

import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 同步任务表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface JobTasksMapper {

    /**
     * 新增
     *
     * @param jobTasks
     */
    Integer insert(JobTasks jobTasks);

    /**
     * 新增
     *
     * @param jobTasks
     */
    Integer insertCapture(JobTasks jobTasks);

    /**
     * 修改
     *
     * @param jobTasks
     */
    Integer update(@Param("jobTasks") JobTasks jobTasks);

    /**
     * 逻辑刪除
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
    JobTasks selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<JobTasks> page(@Param("page") Page<JobTasks> page, @Param("state") Integer state,
            @Param("jobType") Integer jobType, @Param("jobName") String jobName, @Param("beginTime") String beginTime,
            @Param("endTime") String endTime, @Param("code") String code);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state, @Param("jobType") Integer jobType, @Param("jobName") String jobName,
            @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("code") String code);

    /**
     * 修改任务状态
     *
     * @param id
     * @param code
     * @return
     */
    Integer updateState(@Param("id") Long id, @Param("code") String code);

    List<JobTasks> selectList();

    List<JobTasks> selectJobNameList();
}