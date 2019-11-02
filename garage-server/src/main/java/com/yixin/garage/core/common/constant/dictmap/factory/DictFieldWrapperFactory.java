/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yixin.garage.core.common.constant.dictmap.factory;


import java.lang.reflect.Method;

import com.yixin.garage.core.common.factory.ConstantFactory;
import com.yixin.garage.core.common.factory.IConstantFactory;
import com.yixin.garage.core.model.exception.ServiceException;
import com.yixin.garage.core.model.exception.enums.BzExceptionEnum;

/**
 * 字典字段的包装器(从ConstantFactory中获取包装值)
 *
 */
public class DictFieldWrapperFactory {

    public static Object createFieldWrapper(Object parameter, String methodName) {
        IConstantFactory constantFactory = ConstantFactory.me();
        try {
            Method method = IConstantFactory.class.getMethod(methodName, parameter.getClass());
            return method.invoke(constantFactory, parameter);
        } catch (Exception e) {
            try {
                Method method = IConstantFactory.class.getMethod(methodName, Integer.class);
                return method.invoke(constantFactory, Integer.parseInt(parameter.toString()));
            } catch (Exception e1) {
                throw new ServiceException(BzExceptionEnum.ERROR_WRAPPER_FIELD);
            }
        }
    }

}
