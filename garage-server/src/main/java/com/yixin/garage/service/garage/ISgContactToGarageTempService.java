package com.yixin.garage.service.garage;

import com.yixin.garage.dto.api.forLoan.SgPersonDTO;
import com.yixin.garage.entity.garage.SgContactToGarageTemp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author libochen
 * @since 2019-08-05
 */
public interface ISgContactToGarageTempService extends IService<SgContactToGarageTemp> {

    List<String> selectGarageId(String currentUser);

    List<SgPersonDTO> selectContact(String currentUser);
}
