package com.yixin.garage.service.zhongtai;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.dto.zhongtai.SgGarageSendInfoDTO;
import com.yixin.garage.entity.zhongtai.SgSendInfoToZhongtai;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouhang
 * @since 2019-08-29
 */
public interface ISgSendInfoToZhongtaiService extends IService<SgSendInfoToZhongtai> {

    /**
     * @Title: pageQuery
     * @Description: 分页查询
     * @param:
     * @author YixinCapital -- zhouhang
     * @date 2019/8/29 15:02
     */
    IPage<SgGarageSendInfoDTO> pageQuery(SgGarageSendInfoDTO sgGarageSendInfoDTO) throws BzException;

    IPage<SgGarageSendInfoDTO> pageQueryLog(SgGarageSendInfoDTO sgGarageSendInfoDTO) throws BzException;

    /**
     * 定时给中台推送未推送或推送失败的单子。
     */
    void sendInfoToZT() throws BzException;

    /**
     * 手动给中台推送未推送或推送失败的单子。
     */
    InvokeResult<String> sendInfoToZT(SgGarageSendInfoDTO sgGarageSendInfoDTO) throws BzException;
}
