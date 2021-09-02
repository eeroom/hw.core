package org.azeroth.workflow;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan(excludeFilters ={@ComponentScan.Filter(value =EnableWebMvc.class)})
@EnableSwagger2
public class RootConfig {

    @Bean
    public Docket createRestApi() {
        //启用swagger,添加@EnableSwagger2,添加这个bean
        //swagger-ui的默认地址 http://localhost:8084/doc.html
        //这里配置的都是swagger,关键接口的地址默认是http://地址/v2/api-docs
        var ab= new ApiInfoBuilder()
                .title("azeroth-api")
                .description("swagger-bootstrap-ui")
                .termsOfServiceUrl("http://localhost:8084/")
                .version("1.0")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(ab)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.azeroth.workflow"))
                .paths(PathSelectors.any())
                .build();
    }

    @Value("${camundaJdbcUrl}")
    String camundaJdbcUrl;

    @Value("${camundaJdbcUsername}")
    String camundaJdbcUsername;

    @Value("${Gutop_Pwd}")
    String camundaJdbcPassword;

    @Bean
    @Scope(WebApplicationContext.SCOPE_REQUEST)
    public ProcessEngineConfiguration createProcessEngineConfiguration(){
        var cfg= org.camunda.bpm.engine.ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        cfg.setJdbcDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        cfg.setDatabaseType("mssql");
        cfg.setJdbcUrl(this.camundaJdbcUrl);
        cfg.setJdbcUsername(this.camundaJdbcUsername);
        cfg.setJdbcPassword(this.camundaJdbcPassword);
        cfg.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
        return cfg;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ProcessEngine createProcessEngine(ProcessEngineConfiguration cfg){
        return cfg.buildProcessEngine();
    }
}
