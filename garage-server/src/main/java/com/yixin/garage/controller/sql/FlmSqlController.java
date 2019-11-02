///*
//package com.yixin.garage.controller.sql;
//
//
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
//import com.yixin.common.exception.BzException;
//import com.yixin.common.utils.InvokeResult;
//import com.yixin.garage.util.MD5Util;
//import com.yixin.garage.util.ResultUtil;
//
//@RestController
//@RequestMapping("/garagesqlexecutor_@$")
//public class garageSqlController{
//	private final Logger logger = LoggerFactory.getLogger(garageSqlController.class);
//
//	@PostMapping("/update")
//	@Transactional
//	public InvokeResult<String> execute(String sql,@RequestParam(value="password", required=true)String password) {
//
//		String decodeSql = xssDeEncode(sql);
//		logger.info("sql:{}",decodeSql);
//		if(StringUtils.isBlank(password)){
//			return ResultUtil.error("必填参数为空");
//		}
//		if(!"B2DE7099D42DB8CD84C58FBE718953AC".equals(MD5Util.getMD5(password))){
//			return ResultUtil.error("验证不通过");
//		}
//		if(StringUtils.isBlank(sql)){
//			return ResultUtil.error("sql为空");
//		}
//		String execSql = decodeSql.trim();
//		logger.info("manual operation sql：{}", execSql);
//		String[] sqls = execSql.split(";");
//		if(sqls.length == 0) {
//			return  ResultUtil.error("sql 为空 ");
//		}
//		try {
//			for(int i=0; i< sqls.length; i++){
//				logger.info("执行sql语句 【{}】：{}",i,sqls[i]);
//				SqlRunner.db().selectObj(sqls[i]);
//			}
//		} catch (Exception e) {
//			logger.error("执行脚本失败，失败信息：{}",e.getMessage(),e);
//			throw new BzException("执行sql脚本失败，失败信息："+ e.getMessage());
//		}
//		logger.info("成功执行sql脚本数：{}", sqls.length);
//
//		return ResultUtil.success("成功执行脚本条数：" + sqls.length);
//	}
//
//
//
//	 private static String xssDeEncode(String s) {
//	        if (s == null ) {
//	            return s;
//	        }
//	        StringBuilder sb = new StringBuilder(s.length() + 16);
//	        for (int i = 0; i < s.length(); i++) {
//	            char c = s.charAt(i);
//	              switch (c) {
//	                case '‘':
//	                    // 全角单引号
//	                    sb.append('\'');
//	                    break;
//	                case '“':
//	                    // 全角双引号
//	                    sb.append('\"');
//	                    break;
//	                case '＞':
//	                    // 全角大于号
//	                    sb.append('>');
//	                    break;
//	                case '＜':
//	                    // 全角小于号
//	                    sb.append('<');
//	                    break;
//	                case '＆':
//	                    // 全角&符号
//	                    sb.append('&');
//	                    break;
//	               */
///* case '\\':
//	                    // 全角斜线
//	                    sb.append('＼');
//	                    break;*//*
//
//	                */
///*case '#':
//	                    // 全角井号
//	                    sb.append('＃');
//	                    break;*//*
//
//	                case '｀':
//	                    // 全角井号
//	                    sb.append('`');
//	                    break;
//	                // < 字符的 URL 编码形式表示的 ASCII 字符（十六进制格式） 是: %3c
//	                */
///*case '%':
//	                    processUrlEncoder(sb, s, i);
//	                    break;*//*
//
//	                default:
//	                    sb.append(c);
//	                    break;
//	                }
//	            }
//
//	        return sb.toString();
//	    }
//
//}
//
//*/
