package com.yixin.garage.service.inventory.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yixin.common.exception.BzException;
import com.yixin.common.utils.InvokeResult;
import com.yixin.garage.auth.CurrentUser;
import com.yixin.garage.core.base.BaseAssembler;
import com.yixin.garage.dao.AttachSourceMapper;
import com.yixin.garage.dao.inventory.SgInventoryApprovalBillMapper;
import com.yixin.garage.dao.inventory.SgInventoryHomeMapper;
import com.yixin.garage.dto.inventory.SgInventoryBillDTO;
import com.yixin.garage.dto.inventory.SgInventoryDetailsDTO;
import com.yixin.garage.dto.inventory.SgInventoryResultStatisticsDTO;
import com.yixin.garage.entity.AttachSource;
import com.yixin.garage.entity.inventory.SgInventoryApprovalBill;
import com.yixin.garage.entity.inventory.SgInventoryApprovalDetail;
import com.yixin.garage.dao.inventory.SgInventoryApprovalDetailMapper;
import com.yixin.garage.entity.inventory.SgInventoryHome;
import com.yixin.garage.enums.AttachTypeEnum;
import com.yixin.garage.enums.RcAttachmentTypeEnum;
import com.yixin.garage.enums.garage.inventory.InventoryApprovalStateEnum;
import com.yixin.garage.enums.garage.inventory.InventoryBillStateEnum;
import com.yixin.garage.enums.garage.inventory.InventoryResultTypeEnum;
import com.yixin.garage.service.inventory.ISgInventoryApprovalDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yixin.garage.util.ResultUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author libochen
 * @since 2019-10-11
 */
@Service
@Transactional
public class SgInventoryApprovalDetailServiceImpl extends ServiceImpl<SgInventoryApprovalDetailMapper, SgInventoryApprovalDetail> implements ISgInventoryApprovalDetailService {

    @Value("${viewFileServerPath}")
    private String viewFileServerPath;

    @Autowired
    AttachSourceMapper attachSourceMapper;

    @Autowired
    SgInventoryApprovalBillMapper sgInventoryApprovalBillMapper;

    @Autowired
    SgInventoryHomeMapper sgInventoryHomeMapper;

    private Logger logger = LoggerFactory.getLogger(SgInventoryApprovalDetailServiceImpl.class);

    @Override
    public SgInventoryDetailsDTO getDetailView(SgInventoryDetailsDTO dto) throws BzException {
        logger.info("查询主单任务详情getDetailView入参：{}", JSONObject.toJSON(dto));
        SgInventoryDetailsDTO result = new SgInventoryDetailsDTO();
        SgInventoryApprovalDetail detail = this.baseMapper.selectById(dto.getId());
        SgInventoryApprovalBill bill = sgInventoryApprovalBillMapper.selectById(detail.getInventoryApprovalBillId());
        BaseAssembler.mapObjWithoutBaseColumn(detail,result);

        if(detail.getInventory()){
            result.setPandianDate(detail.getPandianDate());
            result.setPandianName(detail.getPandianName());
        }else{
            result.setPandianDate(new Date());
            result.setPandianName(CurrentUser.getCnName());
        }

        //拼装附件信息
        QueryWrapper<AttachSource> vinPhotoAttachSourceQueryWrapper = new QueryWrapper();
        vinPhotoAttachSourceQueryWrapper.eq("IS_DELETED", false);
        vinPhotoAttachSourceQueryWrapper.eq("buss_id", detail.getId());
        vinPhotoAttachSourceQueryWrapper.eq("buss_type", RcAttachmentTypeEnum.INVENTORY_VIN_PHOTO.getValue());
        List<AttachSource> vinPhotoAttList = attachSourceMapper.selectList(vinPhotoAttachSourceQueryWrapper);
        if(CollectionUtils.isNotEmpty(vinPhotoAttList)){
            result.setVinAttId(vinPhotoAttList.get(0).getId());
            result.setVinPhotoView(vinPhotoAttList.get(0).getViewUrl());
            result.setVinPhotoName(vinPhotoAttList.get(0).getSourceAttchName());
            result.setVinPhotoAttId(vinPhotoAttList.get(0).getAttchPath());
        }

        QueryWrapper<AttachSource> carheadPhotoAttachSourceQueryWrapper = new QueryWrapper();
        carheadPhotoAttachSourceQueryWrapper.eq("IS_DELETED", false);
        carheadPhotoAttachSourceQueryWrapper.eq("buss_id", detail.getId());
        carheadPhotoAttachSourceQueryWrapper.eq("buss_type", RcAttachmentTypeEnum.INVENTORY_CARHEAD_PHOTO.getValue());
        List<AttachSource> carheadAttList = attachSourceMapper.selectList(carheadPhotoAttachSourceQueryWrapper);
        if(CollectionUtils.isNotEmpty(carheadAttList)){
            result.setCarHeadAttId(carheadAttList.get(0).getId());
            result.setCarHeadPhotoView(carheadAttList.get(0).getViewUrl());
            result.setCarHeadPhotoName(carheadAttList.get(0).getSourceAttchName());
            result.setCarHeadPhotoAttId(carheadAttList.get(0).getAttchPath());
        }
        return result;
    }


    @Override
    public String saveDetail(SgInventoryDetailsDTO dto) throws BzException {
        logger.info("保存主单任务详情saveDetail入参：{}", JSONObject.toJSON(dto));
        String result = null;
        SgInventoryApprovalDetail detail = this.baseMapper.selectById(dto.getId());

        detail.setIsAppearanceDamage(dto.getIsAppearanceDamage());//外观是否损坏
        detail.setAppearanceDamageDescription(dto.getAppearanceDamageDescription());//外观损坏具体情况说明
        detail.setResultType(dto.getResultType());//盘点结果类型
        detail.setResultTypeDescription(dto.getResultTypeDescription());//盘点结果类型具体情况说明
        detail.setInventory(true);
//        if (null != dto.getPandianDate() && !dto.getPandianDate().equals("")) {
//            detail.setPandianDate(dto.getPandianDate());
//        }else{
//            detail.setPandianDate(new Date());
//        }
        detail.setPandianDate(new Date());

        if (null != dto.getUserName() && !dto.getUserName().equals("")) {
            detail.setPandianName(dto.getPandianName());
        }else{
            detail.setPandianName(CurrentUser.getCnName());
        }

        String detailId = detail.getId();



        //先将之前的附件数据做解绑
        //查询之前的附件是否为空
        QueryWrapper<AttachSource> vinQueryWrapper = new QueryWrapper();
        vinQueryWrapper.eq("IS_DELETED", false);
        vinQueryWrapper.eq("buss_id", detail.getId());
        vinQueryWrapper.eq("buss_type", RcAttachmentTypeEnum.INVENTORY_VIN_PHOTO.getValue());
        List<AttachSource> vinatts = attachSourceMapper.selectList(vinQueryWrapper);
        if(CollectionUtils.isNotEmpty(vinatts)){
            vinatts.get(0).setDeleted(true);
            vinatts.get(0).setBussType(null);
            vinatts.get(0).insertOrUpdate();
        }

        QueryWrapper<AttachSource> carQueryWrapper = new QueryWrapper();
        carQueryWrapper.eq("IS_DELETED", false);
        carQueryWrapper.eq("buss_id", detail.getId());
        carQueryWrapper.eq("buss_type", RcAttachmentTypeEnum.INVENTORY_CARHEAD_PHOTO.getValue());
        List<AttachSource> caratts = attachSourceMapper.selectList(carQueryWrapper);
        if(CollectionUtils.isNotEmpty(caratts)){
            caratts.get(0).setDeleted(true);
            caratts.get(0).setBussType(null);
            caratts.get(0).insertOrUpdate();
        }

        //区分企业微信附件处理
        if (null != dto.getUserName() && !dto.getUserName().equals("")) {
            if(StringUtils.isNotEmpty(dto.getVinPhotoAttId())){
                AttachSource vinAttachSource = new AttachSource();
                vinAttachSource.setBussId(detailId);
//                vinAttachSource.setAttchType(RcAttachmentTypeEnum.INVENTORY_VIN_PHOTO.getValue());
                vinAttachSource.setBussType(RcAttachmentTypeEnum.INVENTORY_VIN_PHOTO.getValue());
                vinAttachSource.setViewUrl(viewFileServerPath + dto.getVinPhotoAttId());
                vinAttachSource.setAttchPath(dto.getVinPhotoAttId());
                vinAttachSource.setSourceAttchName(dto.getVinPhotoName());
                vinAttachSource.insert();
            }
            //车头正面照片
            if(StringUtils.isNotEmpty(dto.getCarHeadPhotoAttId())){
                AttachSource carheadAttachSource = new AttachSource();
                carheadAttachSource.setBussId(detailId);
                carheadAttachSource.setBussType(RcAttachmentTypeEnum.INVENTORY_CARHEAD_PHOTO.getValue());
                carheadAttachSource.setViewUrl(viewFileServerPath + dto.getCarHeadPhotoAttId());
                carheadAttachSource.setAttchPath(dto.getCarHeadPhotoAttId());
                carheadAttachSource.setSourceAttchName(dto.getCarHeadPhotoName());
                carheadAttachSource.insert();
            }
        }else{
            //绑定附件 到详单上
            //车架号照片
            if(StringUtils.isNotEmpty(dto.getVinPhotoAttId())){
                AttachSource vinAttachSource = attachSourceMapper.selectById(dto.getVinPhotoAttId());
                vinAttachSource.setBussId(detailId);
                vinAttachSource.setBussType(RcAttachmentTypeEnum.INVENTORY_VIN_PHOTO.getValue());
                vinAttachSource.insertOrUpdate();
            }
            //车头正面照片
            if(StringUtils.isNotEmpty(dto.getCarHeadPhotoAttId())){
                AttachSource carheadAttachSource = attachSourceMapper.selectById(dto.getCarHeadPhotoAttId());
                carheadAttachSource.setBussId(detailId);
                carheadAttachSource.setBussType(RcAttachmentTypeEnum.INVENTORY_CARHEAD_PHOTO.getValue());
                carheadAttachSource.insertOrUpdate();
            }
        }
        detail.insertOrUpdate();
        return result;
    }

    @Override
    public String otherAdd(SgInventoryDetailsDTO dto) throws BzException {
        logger.info("其他车辆新增otherAdd入参：{}", JSONObject.toJSON(dto));
        String msg = null;
        SgInventoryApprovalDetail detail = new SgInventoryApprovalDetail();
        SgInventoryApprovalBill bill = sgInventoryApprovalBillMapper.selectById(dto.getSgBillId());
        BaseAssembler.mapObjWithoutNull(dto, detail);
        detail.setInventoryApprovalBillId(dto.getSgBillId());
        detail.setInventory(true);
        detail.setGarageAddresss(bill.getGarageAddresss());
        detail.setResultType(InventoryResultTypeEnum.FREE.getValue());
        detail.setManualAdd(true);
        detail.setBrandStr(dto.getBrandStr());
        detail.setApprovalStatus(InventoryApprovalStateEnum.QUALIFIED.getValue());
        detail.insert();
        return msg;
    }

    @Override
    public String otherDelete(SgInventoryDetailsDTO dto) throws BzException {
        String msg = null;
        SgInventoryApprovalDetail detail = this.baseMapper.selectById(dto.getSgBillId());
        this.baseMapper.deleteById(detail.getId());
        return msg;
    }

    @Override
    public InvokeResult<SgInventoryResultStatisticsDTO> resultStatistics(SgInventoryBillDTO dto) throws BzException {
        InvokeResult<SgInventoryResultStatisticsDTO> result = new InvokeResult<SgInventoryResultStatisticsDTO>();
        logger.info("查询主单任务详情resultStatistics入参：{}", JSONObject.toJSON(dto));
        try{
            if (null == dto.getBillNum() || dto.getBillNum().equals("")) {
                result.failure("传入期数不能为空，请检查");
                return result;
            }

            QueryWrapper<SgInventoryHome> queryWrapper = new QueryWrapper();
            queryWrapper.eq("IS_DELETED", false);
            queryWrapper.eq("bill_num", dto.getBillNum());
            List<SgInventoryHome> list = sgInventoryHomeMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(list)){
                dto.setInventoryHomeId(list.get(0).getId());
            }

            SgInventoryResultStatisticsDTO resultStatisticsDTO = new SgInventoryResultStatisticsDTO();
            String zaikuCount = this.baseMapper.resultStatistics(dto, InventoryResultTypeEnum.FREE.getValue(),false);
            resultStatisticsDTO.setNumberOfLibraries(zaikuCount);
            String linshiCount = this.baseMapper.resultStatistics(dto, InventoryResultTypeEnum.TEMP_GARAGE_OUT.getValue(),false);
            resultStatisticsDTO.setTemporaryDeliveryQuantity(linshiCount);
            String weizaikuCount = this.baseMapper.resultStatistics(dto, InventoryResultTypeEnum.NOT_FOUND.getValue(),false);
            resultStatisticsDTO.setQuantityNotInStock(weizaikuCount);
            String xubutuiCount = this.baseMapper.resultStatistics(dto, null,true);
            resultStatisticsDTO.setQuantityToBeAdded(xubutuiCount);
            String stockQuantity = this.baseMapper.resultStatistics(dto, null,false);
            resultStatisticsDTO.setStockQuantity(stockQuantity);

            result.setData(resultStatisticsDTO);
        }catch (Exception e){
            logger.error("查询主单任务详情resultStatistics异常：[{}]", e.getMessage(), e);
            throw new BzException(e.getMessage());

        }
        return result;
    }

    @Override
    public String saveApproveDetail(SgInventoryDetailsDTO dto) throws BzException {
        logger.info("保存审批详情saveApproveDetail入参：{}", JSONObject.toJSON(dto));
        String result = null;
        SgInventoryApprovalDetail detail = this.baseMapper.selectById(dto.getId());
        detail.setApprovalStatus(dto.getApprovalStatus());
        detail.setUnqualifiedReasons(dto.getUnqualifiedReasons());
        detail.insertOrUpdate();
        if (null != dto.getUnqualifiedReasons() && !dto.getUnqualifiedReasons().equals("")) {
            detail.setUnqualifiedReasons(dto.getUnqualifiedReasons());
        }
        return result;
    }

}
