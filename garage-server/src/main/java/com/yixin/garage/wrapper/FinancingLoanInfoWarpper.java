package com.yixin.garage.wrapper;

import java.util.List;
import java.util.Map;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.garage.core.common.factory.ConstantFactory;
import com.yixin.garage.core.model.page.PageResult;
import com.yixin.garage.core.util.Contrast;
import com.yixin.garage.core.util.ToolUtil;
//import com.yixin.garage.kernel.core.base.warpper.BaseControllerWrapper;
//import com.yixin.garage.core.common.constant.factory.ConstantFactory;
//import com.yixin.garage.core.util.Contrast;
//import com.yixin.garage.kernel.core.util.ToolUtil;
//import com.yixin.garage.kernel.model.page.PageResult;
import com.yixin.garage.core.warpper.BaseControllerWrapper;

/**
 * 日志列表的包装类
 *
 * @author fengshuonan
 * @date 2017年4月5日22:56:24
 */
public class FinancingLoanInfoWarpper extends BaseControllerWrapper {

    public FinancingLoanInfoWarpper(Map<String, Object> single) {
        super(single);
    }

    public FinancingLoanInfoWarpper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public FinancingLoanInfoWarpper(Page<Map<String, Object>> page) {
        super(page);
    }

    public FinancingLoanInfoWarpper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        
    }
}
