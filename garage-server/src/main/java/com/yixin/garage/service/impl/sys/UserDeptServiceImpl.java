//package com.yixin.garage.service.impl.sys;
//
//import java.util.Collections;
//import java.util.List;
//
//import com.yixin.garage.dao.UserDeptMapper;
//import com.yixin.garage.entity.DeptArea;
//import com.yixin.garage.entity.UserDept;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.yixin.garage.service.sys.IUserDeptService;
//
///**
// * <p>
// *  用户部门服务实现类
// * </p>
// *
// * @author lizhongxin
// * @since 2019-01-15
// */
//@Service
//public class UserDeptServiceImpl extends ServiceImpl<UserDeptMapper, UserDept> implements IUserDeptService {
//
//	@Override
//	public List<String> getUserFinancingAreaCode(String domainAccount) {
//		if(StringUtils.isBlank(domainAccount))return Collections.emptyList();
//		return this.baseMapper.getUserFinancingAreaCode(domainAccount);
//	}
//
//    @Override
//    public void setUserDept(String userName, DeptArea[] depts) {
//        for(DeptArea deptid : depts){
//            UserDept ud = new UserDept();
//            ud.setDeptId(deptid.getId());
//            ud.setDomainAccount(userName);
//            ud.insert();
//        }
//    }
//
//    @Override
//    public List<String> getUserFinancingDeptId(String domainAccount) {
//        if(StringUtils.isBlank(domainAccount))return Collections.emptyList();
//        return this.baseMapper.getUserFinancingDeptId(domainAccount);
//    }
//
//    @Override
//    @Transactional
//    public void removeUserDept(String userName, DeptArea[] depts) {
//        for(DeptArea deptid : depts){
//            this.remove(new QueryWrapper<UserDept>()
//                    .eq("domain_account", userName)
//                    .eq("dept_id", deptid.getId())
//                   );
//        }
//    }
//
//}
