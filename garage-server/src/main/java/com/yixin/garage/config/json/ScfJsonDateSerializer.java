package com.yixin.garage.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.yixin.common.utils.CommonUtil;

import java.io.IOException;
import java.util.Date;

/**
 * Created by xuhuanjun on 2018/4/14.
 */
public class ScfJsonDateSerializer extends JsonSerializer<Date> {
	public ScfJsonDateSerializer() {
	}

	public void serialize(Date date, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
		jsonGenerator.writeString(CommonUtil.formatDate(date, "yyyy-MM-dd"));
	}
}
