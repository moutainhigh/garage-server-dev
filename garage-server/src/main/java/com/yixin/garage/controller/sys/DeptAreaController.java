//package com.yixin.garage.controller.sys;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.yixin.garage.entity.DeptArea;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Repository;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import com.alibaba.fastjson.JSONObject;
//import com.yixin.common.utils.InvokeResult;
//import com.yixin.garage.dto.sys.UserDeptsDTO;
//import com.yixin.garage.util.ResultUtil;
//import com.yixin.oa.web.dto.UserDto;
//
///**
// * <p>
// * 部门表 前端控制器
// * </p>
// *
// * @author lizhongxin
// * @since 2019-01-02
// */
//@RestController
//@Repository
//@RequestMapping("/deptArea")
//public class DeptAreaController {
//    private final static Logger logger = LoggerFactory.getLogger(DeptAreaController.class);
//
////    @Autowired
////    private IUserDeptService userDeptService;
//
////    @Autowired
////    private ICfgNotifyMailService iCfgNotifyMailService;
//
////    @RequestMapping("/getDeptAreaList")
////    public List<DeptArea> getDeptAreaList() {
////        return deptAreaService.getDeptAreaList();
////    }
//
////    @RequestMapping("/setMail")
////    public InvokeResult<String> setMail(@RequestParam Long departmentId,@RequestParam String userName,
////            @RequestParam String nottifyName,@RequestParam String mail,@RequestParam String notifyType
////            ,@RequestParam String flag) {
////        return ResultUtil.success(iCfgNotifyMailService.setMail(departmentId,userName,flag,
////                nottifyName,mail,notifyType));
////    }
//
//    @RequestMapping("/setUserDept")
//    @ResponseBody
//    public InvokeResult<String> setUserDept(@RequestBody UserDeptsDTO userDept) {
//        logger.info("添加人员大区关系{}", JSONObject.toJSON(userDept));
//        if(StringUtils.isEmpty(userDept.getDomainAccount()) || userDept.getDepts()==null || userDept.getDepts().length==0){
//            return ResultUtil.error("域账号和部门信息都需要填写");
//        }
//        UserDto userDto = getUserDto(userDept.getDomainAccount());
//        if(userDto== null){
//        	return ResultUtil.error("根据域账号未查询到用户信息，请核实！");
//        }
//        userDeptService.setUserDept(userDept.getDomainAccount(), userDept.getDepts());
//        return ResultUtil.success("成功");
//    }
//
//    @RequestMapping("/removeUserDept")
//    @ResponseBody
//    public InvokeResult<String> removeUserDept(@RequestBody UserDeptsDTO userDept) {
//        logger.info("减少人员大区关系{}", JSONObject.toJSON(userDept));
//        if(StringUtils.isEmpty(userDept.getDomainAccount()) || userDept.getDepts()==null || userDept.getDepts().length==0){
//            return ResultUtil.error("域账号和部门信息都需要填写");
//        }
//        userDeptService.removeUserDept(userDept.getDomainAccount(), userDept.getDepts());
//        return ResultUtil.success("成功");
//    }
//
//    /**
//     *
//     * @Description: 查询部门大区
//     * @param flag:1:已经分配的  0:未分配的
//     * @param domainAccount:域账号
//     * @return InvokeResult<String>
//     * @throws
//     * @author YixinCapital -- yangfei02
//     *	       2019年3月12日 下午3:14:40
//     */
////    @GetMapping("/queryUserDept")
////    @ResponseBody
////    public List<DeptArea> queryUserDept(@RequestParam String flag, @RequestParam String domainAccount) {
////        List<DeptArea> list = new ArrayList<>();
////        try{
////            if(StringUtils.isEmpty(domainAccount) || StringUtils.isEmpty(flag)){
////                return list;
////            }
////            list = deptAreaService.getDeptAreaList(flag,domainAccount);
////        }catch(Exception e){
////            ResultUtil.error(e.getMessage());
////        }
////        return list;
////    }
//
//
//    @Value("${usc.findResourceByUser}")
//    private String findResourceByUser;
//    @Autowired
//    private RestTemplate restTemplate;
//
//    /**
//     *
//     * 查询用户信息
//     *
//     * @author YixinCapital--wujt 2017/6/28 16:40
//     *
//     */
//    private UserDto getUserDto(String username) {
//        HttpHeaders headers = new HttpHeaders();
//        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//        headers.setContentType(type);
//        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//        HttpEntity<String> formEntity = new HttpEntity<String>("", headers);
//        String url = findResourceByUser;
//        url = url + "?username=" + username;
//        ResponseEntity<UserDto> responseEntity = restTemplate.postForEntity(url, formEntity,
//                UserDto.class);
//        return responseEntity.getBody();
//    }
//}
