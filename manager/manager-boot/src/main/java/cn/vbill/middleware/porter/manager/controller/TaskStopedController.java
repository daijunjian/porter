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

package cn.vbill.middleware.porter.manager.controller;


import cn.vbill.middleware.porter.manager.ManagerContext;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 告警配置表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
@Api(description = "zabbix任务报警接口")
@RestController
@RequestMapping("/alarm/task")
public class TaskStopedController {

    @GetMapping("/check")
    public void info(HttpServletResponse response) {
        Map<String, List<String>> tasks = ManagerContext.INSTANCE.getStoppedTasks();
        if (null != tasks && !tasks.isEmpty()) {
            try {
                response.getWriter().write(tasks.toString());
                response.getWriter().flush();
            } catch (Throwable e) {
            }
        }
        //return null != tasks && !tasks.isEmpty() ? tasks.toString() : "";
    }
}