package com.gonnichiwa.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 아래 Configuration 작성하여 아래 url로 돌려볼것.
 * http://localhost:8080/swagger-ui.html
 * */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	@Bean
	public Docket swaggerApi(){
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.gonnichiwa.controller"))
				.paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false); // 200, 401, 403, 404 메세지 표시 안함.
	}

	public ApiInfo swaggerInfo(){
		return new ApiInfoBuilder().title("Spring boot restapi project Documentation")
				.description("앱 개발시 사용되는 서버 API에 대한 연동 문서입니다.")
				.licenseUrl("gonnichiwa").licenseUrl("popcafe01.tistory.com").version("1").build();
	}
}
