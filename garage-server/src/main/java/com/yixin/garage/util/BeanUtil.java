package com.yixin.garage.util;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;

public class BeanUtil {
	
	/**
	 * 把Bean里面的String属性为空字符串的置为null。
	 * 
	 * <pre>
	 * 
	 * StrField (""),("  ") 都设置为null   
	 * </pre>
	 * @param <T> Bean类型
	 * @param bean Bean对象
	 * @param ignoreFields 不需要trim的Field名称列表（不区分大小写）
	 */
	public static <T> T setEmptyStrFields2Null(T bean, String... ignoreFields) {
		if (bean == null) {
			return bean;
		}

		final Field[] fields = ReflectUtil.getFields(bean.getClass());
		for (Field field : fields) {
			if (ignoreFields != null && ArrayUtil.containsIgnoreCase(ignoreFields, field.getName())) {
				// 不处理忽略的Fields
				continue;
			}
			if (String.class.equals(field.getType())) {
				// 只有String的Field才处理
				final String val = (String) ReflectUtil.getFieldValue(bean, field);
				if (null != val && StringUtils.isBlank(val)) {
					// Field Value不为null，且首尾有空格才处理
					ReflectUtil.setFieldValue(bean, field, null);
				}
			}
		}

		return bean;
	}
}

