package com.yixin.garage;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yixin.garage.dao.order.SgGarageDetailMapper;
import com.yixin.garage.entity.order.SgGarageDetail;
import com.yixin.garage.enums.garage.RCAllocateGarageSignEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {
	 protected  static  final Logger logger  = LoggerFactory.getLogger(BaseTest.class);

	/**
	 * 注入字典服务
	 */
	@Autowired
	public SgGarageDetailMapper sgGarageDetailMapper;


	@Test
	public void  test(){


		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.eq("IS_DELETED", 0);
		queryWrapper.eq("vin", "123123123");
		queryWrapper.eq("garage_sign", RCAllocateGarageSignEnum.GARAGEIN_SIGN.getValue());
//		queryWrapper.eq("alix_num", dto.getApplyNo());
		List<SgGarageDetail> sgGarageDetails = sgGarageDetailMapper.selectList(queryWrapper);

		System.out.println(JSONObject.toJSONString(sgGarageDetails));

	}

}
