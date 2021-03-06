/**
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

package cn.vbill.middleware.porter.manager.web.handler;

import cn.vbill.middleware.porter.manager.exception.BaseException;
import cn.vbill.middleware.porter.manager.exception.ExceptionCode;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web层异常拦截
 *
 * @author guohongjian[guo_hj@suixingpay.com]
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseMessage inspector(Exception exception, HttpServletRequest request, HttpServletResponse response) {

        if (exception instanceof BaseException) {
            logger.info(exception.getMessage(), exception);
            BaseException e = (BaseException) exception;
            return ResponseMessage.error(e.getMessage(),
                    Integer.valueOf(e.getMessageCode() != null ? e.getMessageCode() : "0"));
        }
        logger.error(exception.getMessage(), exception);
        return ResponseMessage.error(exception.getMessage(), ExceptionCode.EXCEPTION_SYSTEM);

    }

}
