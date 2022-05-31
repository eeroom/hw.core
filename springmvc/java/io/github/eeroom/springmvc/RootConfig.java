package io.github.eeroom.springmvc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@PropertySource(ApplicationContext.CLASSPATH_URL_PREFIX+"application.properties")
@org.springframework.context.annotation.ComponentScan(excludeFilters ={@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = WebConfig.class )})
public class RootConfig {

}
