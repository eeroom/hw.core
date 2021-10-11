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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 这个容器不扫描控制器的bean，不扫描webconfig中定义的bean,
 * 如果不扫，全部交给webconfig的容器去扫，在filter、springmvc的拦截器让容器实例化bean的时候不方便（比如取配置文件数据，注入其它的bean），因为那些里面拿的都是rootconfig对应的容器
 */
@Configuration
@PropertySource(ApplicationContext.CLASSPATH_URL_PREFIX+"application.properties")
@org.springframework.context.annotation.ComponentScan(excludeFilters ={@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = WebConfig.class ),
@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class, RestController.class, ControllerAdvice.class})})
public class RootConfig {
    @Bean
    public org.camunda.bpm.engine.ProcessEngineConfiguration processEngineConfiguration(MapProperties mapProperties){
        var cfg=org.camunda.bpm.engine.ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        cfg.setJdbcDriver(mapProperties.camundaJdbcDriver);
        cfg.setJdbcUrl(mapProperties.camundaJdbcUrl);
        cfg.setJdbcUsername(mapProperties.camundaJdbcUsername);
        cfg.setJdbcPassword(mapProperties.camundaJdbcPwd);
        cfg.setDatabaseType(mapProperties.camundaDatabaseType);
        cfg.setDatabaseSchemaUpdate(mapProperties.camundaDatabaseSchemaUpdate);
        return cfg;
    }

    @Bean
    @Scope(AbstractBeanFactory.SCOPE_PROTOTYPE)
    public org.camunda.bpm.engine.ProcessEngine processEngine(ProcessEngineConfiguration cfg){
        var pe=cfg.buildProcessEngine();
        return pe;
    }

    //用于存放本次请求的当前用户信息，因为AuthenticationHandlerInterceptor里面要用到，他又只能拿到rootconfig对应的容器，所以把这个bean放在这里，
    //如果直接在class上标注@component,rootconfig对应的容器获取不到这个bean，因为rootconfig对应的容器没有扫包，只有webconfig的容器进行了扫包
    @Bean
    @Scope(WebApplicationContext.SCOPE_REQUEST)
    public LoginUserInfo loginUserInfo(){
        return new LoginUserInfo();
    }
}
