package io.github.eeroom.springboot2xcore.自定义配置文件;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.ResourcePropertySource;

public class App {
    public static void main(String[] args) throws Throwable {

        var context=new GenericApplicationContext();
        //中文会乱码，所以额外处理
        var encodedResource=new EncodedResource(context.getResource(ApplicationContext.CLASSPATH_URL_PREFIX+"web.config"),"utf-8");
        var propertySource=new ResourcePropertySource(encodedResource);
        //如果对应的bean是单例，则必须在 context.refresh()之前添加，否则@ConfigurationProperties(prefix = "wch") 标注的bean 取不到这个resource中配置信息
        //因为context中执行了提前实例化单例bean
        //如果对应的bean是多例，则只要在获取bean之前添加，都能正常获取到配置值
        context.getEnvironment().getPropertySources().addLast(propertySource);

        context.registerBean(WebConfigProperty.class,beanDefinition->beanDefinition.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE));
        //增加@ConfigurationProperties 注解配套的 bean后处理起，这样WebConfigProperty的字段才能正确的从配置文件中获取对应的值
        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());
        context.refresh();


        var webconfig= context.getBean(WebConfigProperty.class);
        System.out.println("wch.name="+webconfig.name);
        System.out.println("wch.age="+webconfig.age);
    }
}
