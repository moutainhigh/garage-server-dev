package com.yixin.garage.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Bigecimal格式化为千分位，并保留两位小数，四舍五入，如 11122.33 格式化为：11,122.33
 * Package : com.yixin.scf.core.web.configuration
 * 
 * @author YixinCapital -- chenlong1
 *		   2018年4月10日 下午1:42:32
 *
 */
@Component
@Slf4j
public class JsonBigDecimalThousandthSerializer extends JsonSerializer<BigDecimal> {
	public JsonBigDecimalThousandthSerializer() {
    }

    public void serialize(BigDecimal decimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if(decimal != null){
        	NumberFormat numberFormat = NumberFormat.getNumberInstance();
			numberFormat.setMinimumFractionDigits(2);
        	//四舍五入
        	String formatData=numberFormat.format( decimal.setScale(2, BigDecimal.ROUND_HALF_UP));
        	jsonGenerator.writeString(formatData);
        }
    }
}
