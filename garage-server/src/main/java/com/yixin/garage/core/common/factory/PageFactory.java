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
package com.yixin.garage.core.common.factory;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.common.utils.BaseDTO;
import com.yixin.garage.core.common.constant.state.Order;
import com.yixin.garage.core.util.HttpContext;
import com.yixin.garage.core.util.ToolUtil;

/**
 * BootStrap Table默认的分页参数创建
 *
 * @author fengshuonan
 * @date 2017-04-05 22:25
 */
public class  PageFactory<T> {

    public Page<T> defaultPage() {
        HttpServletRequest request = HttpContext.getRequest();
        String currentStr = request.getParameter("current");
        int current = StringUtils.isNotBlank(currentStr) ? Integer.valueOf(currentStr): 1 ;     //当前页  ,默认第一页
        String sizeStr = request.getParameter("size");
        int pageSize = StringUtils.isNotBlank(sizeStr) ? Integer.valueOf(sizeStr) : 10;   //每页显示条数
        String sort = request.getParameter("sort");         //排序字段名称
        String order = request.getParameter("order");       //asc或desc(升序或降序)
        if (ToolUtil.isEmpty(sort)) {
        	return new Page<>(current, pageSize);
        } else {
            Page<T> page = new Page<>(current, pageSize);
            if (Order.ASC.getDes().equals(order)) {
                page.setAsc(sort);
            } else {
                page.setDesc(sort);
            }
            return page;
        }
    }
    
    public  Page<T> defaultPage(BaseDTO dto) {
        int current = dto.getCurrent();     //当前页  ,默认第一页
        int pageSize = dto.getRowCount();   //每页显示条数
        return new Page<>(current, pageSize);
    }
    
    
}
