package com.yixin.garage.config.json;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.yixin.common.utils.CommonUtil;

/**
 * Created by xuhuanjun on 2018/4/15.
 */
@Slf4j
public class ScfJsonDateDeserializer extends JsonDeserializer<Date> {

	public ScfJsonDateDeserializer() {
	}

	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		Date date = null;
		String text = jp.getText();
		if (StringUtils.isBlank(text)) {
			return date;
		} else {
			try {
				date = CommonUtil.parseDate(text, "yyyy-MM-dd");
			} catch (ParseException e) {
				log.error("日期型参数反序列化错误应匹配【yyyy-MM-dd】格式", e);
			}
		}

		return date;
	}
}
