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
                .title("JwtDemo Api 文檔")
                // 描述
                .description("JwtDemo 文檔<br>" +
                        "<b>備註：<br>" +
                        "(1)頁面上的\"文檔管理\"要配置\"全局参数设置\"(为了模擬已登入狀態),配置后<a href=\"#\">重刷頁面</a>即可生效<br></b>" +
                        "参數名稱:Authorization<br>" +
                        "参数值:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaHJpcyIsImlhdCI6MTY0MTIwMTAwNywiZXhwIjoxNjQxMjg3NDA3fQ.XAW0kSkw0CWxreJJLNPerHUFQyxYmvIsay7-KmoRCn30MV3SvSh2MtqnerESeZTKn7fIMXVXb3hll_CdzMjB2g<br>" +
                        "(可以使用<a href=\"#/default/内部测试接口/createSessionUsingGET\">内部测试接口-产生BestPay-Session</a>)<br>" +
                        "<b><font color=\"red\">(2)生產環境要屏蔽swagger相關資源，要在properties添加以下参数<br></font></b>" +
                        "knife4j.production=true<br>")
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
