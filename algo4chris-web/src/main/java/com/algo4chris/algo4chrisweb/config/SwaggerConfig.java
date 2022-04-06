package com.algo4chris.algo4chrisweb.config;

import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@EnableKnife4j
@EnableSwagger2
@Configuration
public class SwaggerConfig extends DefaultSwaggerConfig{

	@Bean(value = "defaultApi")
	public Docket defaultApi() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(PathSelectors.any())
				.build();
		return docket;
	}

	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
        		// 标题
                .title("Algo4chris相關文檔")
                // 描述
                .description("Algo4chris相關文檔")
                // 版本
                .version("1.0")
                .contact(new Contact("Chris","https://www.linkedin.com/in/chris-luo-b4b350189/","chrislo5311@gmail.com"))
                .build();
    }
	
	@Bean
    public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
		//处理swagger请求分页问题
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }
            @Override
            public List<AlternateTypeRule> rules() {
                return new ArrayList(Collections.singleton(newRule(resolver.resolve(Pageable.class), resolver.resolve(Page.class))));
            }
        };
    }
	
	@Data
	@ApiModel
    static class Page {
        @ApiModelProperty("頁碼(0..N)")
        private Integer page;
 
        @ApiModelProperty("每頁筆數")
        private Integer size;
 
        @ApiModelProperty("排序")
        private List<String> sort;
    }
	
}
