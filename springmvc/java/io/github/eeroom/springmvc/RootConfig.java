package io.github.eeroom.springmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan(excludeFilters ={@ComponentScan.Filter(value = EnableWebMvc.class)})
@EnableSwagger2
public class RootConfig {

    @Bean
    public Docket createRestApi() {
        //启用swagger,添加@EnableSwagger2,添加这个bean
        //默认地址 http://localhost:8083/doc.html
        var ab= new ApiInfoBuilder()
                .title("azeroth-api")
                .description("swagger-bootstrap-ui")
                .termsOfServiceUrl("http://localhost:8083/")
                .version("1.0")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(ab)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.github.eeroom.springmvc.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
