package lt.ignitis.GintautasButkus;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    
    @Bean
	public Docket getBean() {
		return new Docket(DocumentationType.SWAGGER_2).select().paths(regex("/api.*")).build().apiInfo(getInfo());
	}
	
	private ApiInfo getInfo() {
		return new ApiInfoBuilder().title("Ignitis Messaging Service").description("Message Service").license("Gintautas Butkus").build();
	}
}
