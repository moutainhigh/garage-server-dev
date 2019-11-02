package com.yixin.garage.core.base;

import java.lang.ref.WeakReference;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;

import com.yixin.common.utils.BaseDTO;
import com.yixin.common.utils.YxCurrentUserDTO;
import com.yixin.garage.auth.CurrentUser;

import cn.hutool.core.bean.BeanUtil;

/**
 * 基础Assembler
 * 
 * Package : com.yixin.common.system.utils
 * 
 * @author YixinCapital -- wujt 2016年7月8日 下午2:31:42
 *
 */
public class BaseAssembler {

	private static Mapper beanMapper = new DozerBeanMapper();

	public static void mapObj(Object obj1, Object obj2) {
		beanMapper.map(obj1, obj2);
		dealObject(obj1, obj2);
	}

	private static void dealObject(Object obj1, Object obj2) {
		if (obj1 instanceof BaseDTO && obj2 instanceof BaseEntity) { // DTO转Entity时，处理创建人和更新人信息
			BaseDTO obj12 = (BaseDTO) obj1;
			BaseEntity obj21 = (BaseEntity) obj2;
			if (StringUtils.isNotBlank(obj12.getId())) { // 更新
				dto2entity4update(obj12, obj21);
			} else { // 新增
				dto2entity4create(obj12, obj21);
			}
		}
	}

	/**
	 * 不复制null值
	 * 
	 * @param sources
	 * @param destination
	 * @author YixinCapital -- wujt 2016年9月27日 下午6:56:38
	 */
	public static void mapObjWithoutNull(final Object sources, final Object destination) {
		WeakReference weakReference = new WeakReference(new DozerBeanMapper());
		DozerBeanMapper mapper = (DozerBeanMapper) weakReference.get();
		mapper.addMapping(new BeanMappingBuilder() {
			public void configure() {
				mapping(sources.getClass(), destination.getClass(),
						TypeMappingOptions.mapNull(false), TypeMappingOptions.mapEmptyString(true));
			}
		});
		mapper.map(sources, destination);
		dealObject(sources, destination);
		mapper.destroy();
		weakReference.clear();
	}
	
	/**
	 * 将dto转化为entity，新增时调用，可以自动赋值创建人信息 和修改人信息
	 * 
	 * @param obj1
	 * @param obj2
	 * @author YixinCapital -- wujt 2016年6月12日 上午11:31:01
	 */
	private static void dto2entity4create(BaseDTO obj1, BaseEntity obj2) {
	    if (CurrentUser.isSMAvailable()){
	        YxCurrentUserDTO currentUser = CurrentUser.getCurrentUserDTO();
	        if (currentUser != null) {
	            String departmentId = currentUser.getDepartmentId();
	            if (departmentId != null && !"".equals(departmentId)) {
	                obj2.setCreatorDepartmentId(Long.valueOf(departmentId));
	            }
	            obj2.setCreatorDepartmentName(currentUser.getDepartmentName());
	            obj2.setCreatorId(currentUser.getUserAccount());
	            obj2.setCreatorName(currentUser.getUserName());
	            if (departmentId != null && !"".equals(departmentId)) {
	                obj2.setUpdatorDepartmentId(Long.valueOf(departmentId));
	            }
	            obj2.setUpdatorDepartmentName(currentUser.getDepartmentName());
	            obj2.setUpdatorId(currentUser.getUserAccount());
	            obj2.setUpdatorName(currentUser.getUserName());
	        }
	    }
	}

	/**
	 * 将dto转化为entity，修改时调用，可以自动赋值修改人信息
	 * 
	 * @param obj1
	 * @param obj2
	 * @author YixinCapital -- wujt 2016年6月12日 上午11:31:53
	 */
	private static void dto2entity4update(BaseDTO obj1, BaseEntity obj2) {
	    if (CurrentUser.isSMAvailable()){
	        YxCurrentUserDTO currentUser = CurrentUser.getCurrentUserDTO();
	        if (currentUser != null) {
	            String departmentId = currentUser.getDepartmentId();
	            if (departmentId != null && !"".equals(departmentId)) {
	                obj2.setUpdatorDepartmentId(Long.valueOf(departmentId));
	            }
	            obj2.setUpdatorDepartmentName(currentUser.getDepartmentName());
	            obj2.setUpdatorId(currentUser.getUserAccount());
	            obj2.setUpdatorName(currentUser.getUserName());
	        }
	    }
	}
	
	/**
     * 
     * @Description: 把源对象中的属性复制到目标对象中,但是不复制源对象中的null属性和基础属性
     *               基础属性包括:id,createTime,creatorDepartmentId,creatorDepartmentName
     *               creatorId,creatorName,deleted,updateTime,updatorDepartmentId,
     *               updatorDepartmentName,updatorId,updatorName,version
     *               此处字段属性名称区分大小写
     * @param sources
     * @param destination void
     * @throws 
     * @author YixinCapital -- yangfei02
     *         2019年1月30日 下午3:46:51
     */
    public static void mapObjWithoutNullAndBaseColumn(final Object sources, final Object destination) {
        Map<String, Object> beanToMap = BeanUtil.beanToMap(sources, false, true);
        BeanUtil.copyProperties(beanToMap, destination, 
                "id","createTime","creatorDepartmentId","creatorDepartmentName",
                "creatorId","creatorName","deleted","updateTime","updatorDepartmentId",
                "updatorDepartmentName","updatorId","updatorName","version");
    }
    
    /**
     * 
     * @Description: 把源对象中的属性复制到目标对象中,但是不复制源对象中的基础属性,但是不会忽略null
     *               基础属性包括:id,createTime,creatorDepartmentId,creatorDepartmentName
     *               creatorId,creatorName,deleted,updateTime,updatorDepartmentId,
     *               updatorDepartmentName,updatorId,updatorName,version
     *               此处字段属性名称区分大小写
     * @param sources
     * @param destination void
     * @throws 
     * @author YixinCapital -- yangfei02
     *	       2019年1月30日 下午6:12:03
     */
    public static void mapObjWithoutBaseColumn(final Object sources, final Object destination) {
        BeanUtil.copyProperties(sources, destination, 
                "id","createTime","creatorDepartmentId","creatorDepartmentName",
                "creatorId","creatorName","deleted","updateTime","updatorDepartmentId",
                "updatorDepartmentName","updatorId","updatorName","version");
    }
    
    /**
     * 
     * @Description: 把源对象中的属性复制到目标对象中,但是不复制源对象中的null属性和ignoreColums指定的属性
     * @param sources
     * @param destination
     * @param ignoreColums 指定的属性名称,区分大小写
     * @throws 
     * @author YixinCapital -- yangfei02
     *	       2019年1月30日 下午4:36:00
     */
    public static void mapObjWithoutNullAndTheCs(final Object sources, final Object destination, String... ignoreColums) {
        Map<String, Object> beanToMap = BeanUtil.beanToMap(sources, false, true);
        BeanUtil.copyProperties(beanToMap, destination,ignoreColums);
    }

	/*public static void main(String[] args){
	    FinancingRepayPlan sources = new FinancingRepayPlan();
	    sources.setId("12121212121");
	    sources.setVersion(1);
	    sources.setApproveDate(new Date());
	    
	    FinancingRepayPlan target = new FinancingRepayPlan();
	    target.setApproveStatus("ssss");
	    
	    
	    Object tmp = target;
	    
	    
	    Map<String, Object> beanToMap = BeanUtil.beanToMap(sources, false, true);
	    //BeanUtil.mapToBean(beanToMap, target, false);
	    mapObjWithoutBaseColumn(sources, target);
	    //mapObjWithoutNull(beanToMap, target);
	    
	    System.out.println(target.getId());
	    System.out.println(target.getVersion());
	    System.out.println(target.getApproveDate());
	    System.out.println(target.getApproveStatus());
	}*/
}
