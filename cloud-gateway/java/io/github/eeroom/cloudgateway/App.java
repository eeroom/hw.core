package io.github.eeroom.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableZuulProxy
@EnableSwagger2
public class App {
    public static void main(String[] args){
        SpringApplication.run(App.class,args);
    }

    @Bean
    public Docket docket(){
        var apiInfo= new ApiInfoBuilder()
                .title("cloud-gateway的文档-title")
                .description("cloud-gateway的文档-description")
                .termsOfServiceUrl("http://cloud-gateway")
                .version("1.0")
                .build();
        var docket= new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
