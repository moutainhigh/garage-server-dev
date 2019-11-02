package com.yixin.garage.service.api;


import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.api.forLoan.SgGarageForLoanGarageInDTO;
import com.yixin.garage.dto.api.forLoan.SgGarageForLoanGarageOutDTO;
import com.yixin.garage.dto.api.forLoan.SgQueryVehicleIfoStatDTO;
import com.yixin.garage.util.excel.inptVO.DataCreateImportVO;


public interface SgGarageForLoanService {


    /**
    * inGarage(接收融后入库指令接口)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/8/13 17:06
    */
    InvokeResult<String> inGarage (SgGarageForLoanGarageInDTO dto) throws BzException;


    /**
    * outGarage(接收融后出库指令接口)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<java.lang.String>
    * @author: YixinCapital -- libochen
    * 2019/8/13 17:07
    */
    InvokeResult<String> outGarage (SgGarageForLoanGarageOutDTO dto) throws BzException;


    /**
    * outGarage(这里用一句话描述这个方法的作用)
    * @param dto
    * @return
    * com.yixin.common.utils.InvokeResult<com.yixin.garage.dto.api.forLoan.SgQueryVehicleIfoStatDTO>
    * @author: YixinCapital -- libochen 
    * 2019/8/20 14:56 
    */
    InvokeResult<SgQueryVehicleIfoStatDTO> queryVehicleStat (SgQueryVehicleIfoStatDTO dto) throws BzException;



    InvokeResult<String> dateCreate (DataCreateImportVO dto) throws BzException;


}
