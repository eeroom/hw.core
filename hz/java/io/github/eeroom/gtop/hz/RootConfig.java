package io.github.eeroom.gtop.hz;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个容器不扫描控制器的bean，不扫描webconfig中定义的bean,
 * 如果不扫，全部交给webconfig的容器去扫，在filter、springmvc的拦截器让容器实例化bean的时候不方便（比如取配置文件数据，注入其它的bean），因为那些里面拿的都是rootconfig对应的容器
 * 2021年10月11日更新：简化扫包，WebConfig不扫包，通过aspNetRequestMappingHandler.setDetectHandlerMethodsInAncestorContexts(true)，作用是查找controller的时候检测父容器，
 * 这样api也能正常，之前不扫包swagger不能正常工作的原因就是（控制器被父容器扫描，然后aspNetRequestMappingHandler的容器是webconfig,查找控制器的时候没有去检测父容器）
 */
@Configuration
@PropertySource(ApplicationContext.CLASSPATH_URL_PREFIX+"application.properties")
@org.springframework.context.annotation.ComponentScan(excludeFilters ={@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = WebConfig.class )})
public class RootConfig {

    /**
     * ProcessEngineConfiguration是定义在camunda类库中类，我们想让spring容器管理其实例，只能使用这个方式定义bean,没法使用@component标注
     * @param config
     * @return
     */
    @Bean
    public org.camunda.bpm.engine.ProcessEngineConfiguration processEngineConfiguration(AppConfig config){
        var cfg=org.camunda.bpm.engine.ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        cfg.setJdbcDriver(config.camundaJdbcDriver);
        cfg.setJdbcUrl(config.camundaJdbcUrl);
        cfg.setJdbcUsername(config.camundaJdbcUsername);
        cfg.setJdbcPassword(config.camundaJdbcPwd);
        cfg.setDatabaseType(config.camundaDatabaseType);
        cfg.setDatabaseSchemaUpdate(config.camundaDatabaseSchemaUpdate);
        return cfg;
    }

    @Bean
    public org.camunda.bpm.engine.ProcessEngine processEngine(ProcessEngineConfiguration cfg){
        var pe=cfg.buildProcessEngine();
        return pe;
    }

    //用于存放本次请求的当前用户信息，因为AuthenticationHandlerInterceptor里面要用到，他又只能拿到rootconfig对应的容器，所以把这个bean放在这里，
    //如果直接在class上标注@component,rootconfig对应的容器获取不到这个bean，因为rootconfig对应的容器没有扫包，只有webconfig的容器进行了扫包
    //2021年10月11日，通过扫包配置优化，这种场景不需要在这里再次定义bean，直接使用@component标注，rootconfig的容器已经扫描了这个bean
//    @Bean
//    @Scope(WebApplicationContext.SCOPE_REQUEST)
//    public LoginUserInfo loginUserInfo(){
//        return new LoginUserInfo();
//    }

    /**
     * MydispatherServlet中把一些需要用到的信息写到字典，方便后续使用
     * 例如：AspNetHandlerMethodArgumentResolver需要根据当前请求的POST,GET，content-type等信息决定是否解析当前请求的参数
     * 这里因为没有自定义一个类，而是基于hashmap，所有只能使用这个方式定义bean,没法使用@component标注
     * @return
     */
    @Bean
    @Scope(WebApplicationContext.SCOPE_REQUEST)
    public Map<String,Object> httpContext(){
        var mp=new HashMap<String,Object>();
        return mp;
    }
}
