//package com.yixin.garage.service.sys;
//
//import java.util.List;
//
//import com.yixin.garage.entity.DeptArea;
//
///**
// * <p>
// *  服务类
// * </p>
// *
// * @author lizhongxin
// * @since 2019-01-15
// */
//public interface IUserDeptService extends IService<UserDept> {
//
//	/**
//	 *
//	 * @Title: getUserFinancingAreaCode
//	 * @Description: 获取用户 融资业务所属区域编码
//	 * @param domainAccount
//	 * @return     List<String>
//	 * @author YixinCapital -- lizhongxin
//	 *	       2019年2月17日 上午11:01:40
//	 */
//	List<String> getUserFinancingAreaCode(String domainAccount);
//
//	/**
//	 *
//	 * @Description: 设置人员部门
//	 * @param userName
//	 * @param depts
//	 * @throws
//	 * @author YixinCapital -- yangfei02
//	 *	       2019年2月18日 下午4:14:28
//	 */
//	void setUserDept(String userName, DeptArea[] depts);
//
//	/**
//     *
//     * @Description: 减少人员部门
//     * @param userName
//     * @param depts
//     * @throws
//     * @author YixinCapital -- yangfei02
//     *         2019年2月18日 下午4:14:28
//     */
//	void removeUserDept(String userName, DeptArea[] depts);
//
//	/**
//	 *
//	 * @Description: 获取用户 融资业务所属部门id
//	 * @param domainAccount
//	 * @return List<String>
//	 * @throws
//	 * @author YixinCapital -- mjj
//	 *	       2019年2月25日 下午4:16:16
//	 */
//	List<String> getUserFinancingDeptId(String domainAccount);
//}
