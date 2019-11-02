package com.yixin.garage.controller.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.yixin.garage.config.cache.CacheConfig.CacheManagerNames;
import com.yixin.garage.config.cache.CacheConfig.EhCacheNames;
import com.yixin.garage.config.property.SysRoleProperties;
import com.yixin.garage.core.config.properties.AppNameProperties;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/health")
public class HealthController {
	 private static final Logger LOGGER = LoggerFactory.getLogger(HealthController.class);
	
	 
	@GetMapping("/heartbeat")
	@ApiOperation(value = "服务心跳检测接口", notes = "简单接口描述，检测服务状态", code = 200, produces = "application/json")
	public String heartBeat(){
		LOGGER.info("I am fine! ");
		return "I am fine!!";
	}
	
	@Autowired
	private AppNameProperties appName;
	@Value("${spring.application.name}")
	private String applicationName;
	
	@GetMapping("/appName")
	@Cacheable(value=com.yixin.garage.cache.CacheNames.CONSTANT,cacheManager=CacheManagerNames.REDIS_CACHE_MANAGER)
	public String appName(String username){
		ehcache("ere");
		LOGGER.info("username:{}",username);
		LOGGER.info("appName:{}",appName.getName());
		LOGGER.info("applicationName:{}",applicationName);
		return appName.getName()+";"+applicationName;
	}
	
	@GetMapping("/ehcache")
	@Cacheable(value=EhCacheNames.CACHE_10MINS )
	public String ehcache(String username){
		
		LOGGER.info("username:{}",username);
		LOGGER.info("appName:{}",appName.getName());
		LOGGER.info("applicationName:{}",applicationName);
		return appName.getName()+";"+applicationName;
	}
	// 获取id 序列
	@GetMapping("/getNextIds")
	public List<String> getNextIds(int n){
		List<String> ids = new ArrayList<>();
		if(n>1){
			for(int i=0; i < n ; i++){
				String idStr = IdWorker.getIdStr();
				LOGGER.info("generate id: {}",idStr);
				ids.add(idStr);
			}
		}else{
			String idStr = IdWorker.getIdStr();
			LOGGER.info("generate id: {}",idStr);
			ids.add(idStr);
		}
		return ids;
	}


//	@Autowired
//	private IFinancingProjectService projectService;
//	//提供给测试修改项目日期接口，上线前注释掉
//	@GetMapping("/updateProjDate")
//	public String updateProjDate( String projName,String meet,String stamp, String draw){
//		StringBuffer result = new StringBuffer();
//		LOGGER.info("修改融资项目：{} ,预提日期：{},用印日期：{},预提日期：{}",projName,meet,stamp,draw);
//		if(StringUtils.isBlank(projName)) return "项目名称为空，请核实！";
//		List<FinancingProject> list = projectService.list(new QueryWrapper<FinancingProject>().eq("proj_name", projName));
//		if(CollectionUtil.isEmpty(list))return "未查询到对应的项目，请核实项目名称";
//		if(list.size()>1)return "该项目名称存在多个融资项目，请核实";
//		FinancingProject project = list.get(0);
//		boolean updated = false;
//		if(StringUtils.isNotEmpty(meet)){
//			project.setMeetEstDate(DateUtil.StringToDate(meet));
//			result.append("设置上会日期为:").append(meet).append("<br>");
//			updated = true;
//		}
//		if(StringUtils.isNotEmpty(stamp)){
//			project.setStampEstDate(DateUtil.StringToDate(stamp));
//			result.append("设置用印日期为:").append(stamp).append("<br>");
//			updated = true;
//		}
//		if(StringUtils.isNotEmpty(draw)){
//			project.setDrawEstDate(DateUtil.StringToDate(draw));
//			result.append("设置预提日期为:").append(draw).append("");
//			updated = true;
//		}
//		if(updated ) project.updateById();
//
//		return result.toString();
//	}
	
//	@Autowired
//    private ITestDateCfgService testDateCfgService;
    /**
     * 
     * @Description: 提供给测试修改garage日期，生产环境的不允许修改
     * @param env
     * @param garageDate
     * @return String
     * @throws 
     * @author YixinCapital -- yangfei02
     *	       2019年3月15日 下午4:25:39
     */
//    @GetMapping("/updategarageDate/{env}/{garageDate}")
//    public String updateProjDate(@PathVariable(value = "env") String env, @PathVariable(value = "garageDate") String garageDate){
//        LOGGER.info("环境：{} ,设置日期：{}",env,garageDate);
//        if("pro".equals(env)){
//            return "生产环境不允许修改";
//        }
//        Map<String, Object> filterMap = new HashMap<>() ;
//        filterMap.put("environment", env);
//        filterMap.put("type", "1");
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.allEq(filterMap,false);
//        TestDateCfg one = testDateCfgService.getOne(queryWrapper);
//        if(one == null){
//            one = new TestDateCfg();
//            one.setType("1");
//            one.setEnvironment(env);
//        }
//        one.setgarageDate(DateUtil.StringToDate(garageDate, "yyyyMMdd"));
//        one.insertOrUpdate();
//
//        return "成功";
//    }

	@Autowired
    private SysRoleProperties sysRole;

    @GetMapping("/getRole")
    public Map<String, String> getRole(){
    	LOGGER.info("getRole test66666666666");
        return  sysRole.getFinancingRole();
    }
    
//
//    @Autowired
//	private IFinancingLoanInfoService financingLoanInfoService;
//
//    @Autowired
//    private ICfgYxEntityService cfgYxEntityService;
    /**
     * 
     * @Description: 初始化融资项目接口 
     */
//    @GetMapping("/initProjectInfo1")
//    @Transactional
//    public String initProjectInfo(){
//		LOGGER.info("初始化融资项目信息开始。。。");
//		StringBuilder sb = new StringBuilder();
//		List<FinancingLoanInfo> list = financingLoanInfoService.list();
//		Map<String,String>  entityNameCodeMap = new HashMap<>();
//		if(CollUtil.isNotEmpty(list)){
//			for(FinancingLoanInfo loan : list){
//				FinancingProject project = new FinancingProject();
//				project.setProjName(loan.getProjName());
//				project.setProjType(loan.getProjType());
//				if("关联方".equals(loan.getUpdatorName()) || "其它".equals(loan.getUpdatorName())){
//					project.setProjType(FinProjTypeEnum.RELATED_PARTY.getTypeCode());
//				}else if ("银行类".equals(loan.getUpdatorName())){
//					project.setProjType(FinProjTypeEnum.BANK.getTypeCode());
//				}else if ("金租类".equals(loan.getUpdatorName())){
//					project.setProjType(FinProjTypeEnum.GOLD_RENT.getTypeCode());
//				}else if ("信托类".equals(loan.getUpdatorName())){
//					project.setProjType(FinProjTypeEnum.TRUST.getTypeCode());
//					project.setLimitType(FinLimitTypeEnum.LIMIT.getTypeCode());
//				}else if ("ABS类".equals(loan.getUpdatorName())){
//					project.setProjType(FinProjTypeEnum.ABS.getTypeCode());
//
//				}else {
//					LOGGER.info("项目类型比较特殊：{}",loan.getUpdatorName());
//				}
//				if(project.getLimitType()==null){
//					project.setLimitType(FinLimitTypeEnum.OWN.getTypeCode());
//				}
//
//				loan.setProjType(project.getProjType());
//				loan.setProjLimitType(project.getLimitType());
//				project.setLoanEntityName(loan.getLoanEntityName());
//				String code  = entityNameCodeMap.get(loan.getLoanEntityName());
//				if(code == null){
//					code = cfgYxEntityService.getEntityCodeByName(loan.getLoanEntityName());
//					entityNameCodeMap.put(loan.getLoanEntityName(), code);
//				}
//				project.setLoanEntityId(code);
//				loan.setLoanEntityCode(code);
//				code  = entityNameCodeMap.get(loan.getLoanEntityName());
//				project.setWithdrawEntityName(loan.getWithdrawEntityName());//融资主体
//
//
//				project.setSetupDate(loan.getValueDate());
//				project.setMeetEstDate(loan.getValueDate());
//				project.setStampEstDate(loan.getValueDate());
//				project.setDrawEstDate(loan.getValueDate());
//				project.setProjFinAmount(loan.getPrincipalAmount());
//				project.setProjStatus("1");
//				project.setAreaId("100105");
//
//				project.insert();
//				loan.setProjectId(project.getId());
//				loan.updateById();
//
//			}
//
//
//		}
//
//		return sb.toString();
//	}
    
//    @Autowired
//	private IFinancingProjectService financingProjectService;
//
//    @GetMapping("/judgeProjectStatus")
//    public String judgeProjectStatus(){
//    	financingProjectService.judgeProjectStatus();
//    	return "执行成功";
//	}
}

