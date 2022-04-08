package com.algo4chris.algo4chriscommon.config;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

@Slf4j
public class DefaultSwaggerConfig implements ModelPropertyBuilderPlugin {
	
	@Override
	public boolean supports(DocumentationType delimiter) {
		return true;
	}

	@Override
	public void apply(ModelPropertyContext context) {
		// 获取当前字段的类型
		final Class<?> fieldType = context.getBeanPropertyDefinition().get().getField().getRawType();
	}
	
}
