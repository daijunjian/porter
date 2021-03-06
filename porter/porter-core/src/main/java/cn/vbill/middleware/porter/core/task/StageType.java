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

package cn.vbill.middleware.porter.core.task;

/**
 * 工作的阶段
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 10:56
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 10:56
 */
public enum  StageType {
    SELECT, EXTRACT, TRANSFORM, LOAD, DB_CHECK;

    public boolean isSelect() {
        return this.equals(StageType.SELECT);
    }

    public boolean isExtract() {
        return this.equals(StageType.EXTRACT);
    }

    public boolean isTransform() {
        return this.equals(StageType.TRANSFORM);
    }

    public boolean isLoad() {
        return this.equals(StageType.LOAD);
    }

    public boolean isDbCheck() {
        return this.equals(StageType.DB_CHECK);
    }
}
