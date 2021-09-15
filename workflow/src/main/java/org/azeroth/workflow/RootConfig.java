package org.azeroth.workflow;

import com.fasterxml.classmate.TypeResolver;
import org.azeroth.workflow.swagger2.AspNetHandlerMethodResolver;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ContextResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@PropertySource(ApplicationContext.CLASSPATH_URL_PREFIX+"application.properties")
public class RootConfig {
    @Value("${camundaJdbcDriver}")
    String camundaJdbcDriver;

    @Value("${camundaJdbcUrl}")
    String camundaJdbcUrl;

    @Value("${camundaJdbcUsername}")
    String camundaJdbcUsername;

    @Value("${camundaJdbcPwd}")
    String camundaJdbcPwd;

    @Value("${camundaDatabaseType}")
    String camundaDatabaseType;

    @Value("${camundaDatabaseSchemaUpdate}")
    String camundaDatabaseSchemaUpdate;

    @Bean
    public org.camunda.bpm.engine.ProcessEngineConfiguration processEngineConfiguration(){
        var cfg=org.camunda.bpm.engine.ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        cfg.setJdbcDriver(this.camundaJdbcDriver);
        cfg.setJdbcUrl(this.camundaJdbcUrl);
        cfg.setJdbcUsername(this.camundaJdbcUsername);
        cfg.setJdbcPassword(this.camundaJdbcPwd);
        cfg.setDatabaseType(this.camundaDatabaseType);
        cfg.setDatabaseSchemaUpdate(this.camundaDatabaseSchemaUpdate);
        return cfg;
    }

    @Bean
    @Scope(AbstractBeanFactory.SCOPE_PROTOTYPE)
    public org.camunda.bpm.engine.ProcessEngine processEngine(ProcessEngineConfiguration cfg){
        var pe=cfg.buildProcessEngine();
        return pe;
    }

}
