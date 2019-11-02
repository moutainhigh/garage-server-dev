package com.yixin.garage.config.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
/**
 * 将一个可能包含千分位的数字转换为不含千分位的形式,如13,000.00 输出：13000.00
 * Package : com.yixin.scf.core.web.configuration
 * 
 * @author YixinCapital -- chenlong1
 *		   2018年4月10日 下午2:00:03
 *
 */
@Component
public class JsonBigDecimalThousandthDeserializer extends JsonDeserializer<BigDecimal> {
	public JsonBigDecimalThousandthDeserializer() {

	}

	public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String text = jp.getText();
		if (StringUtils.isBlank(text)) {
			return null;
		}
		try {
			double data = new DecimalFormat().parse(text).doubleValue();
			return new BigDecimal(String.valueOf(data));
		} catch (Throwable e) {
			throw new IOException(e);
		}
	}

}
