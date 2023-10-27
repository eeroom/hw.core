package io.github.eeroom.springboot2xcore.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class TestSS {
    public static void main(String[] args){

        var context= SpringApplication.run(TestSS.class);
        var lstloader= context.getBeanFactory().getBeansOfType(RequestMappingHandlerMapping.class);
        lstloader.values().forEach((x)->System.out.println(x));
        System.out.println(context.getEnvironment().getProperty("msg"));
        var msg= context.getBean(TestSS.class).msg;
        System.out.println(msg);
    }

    @Value("${msg}")
    String msg;

    public org.springframework.boot.env.PropertySourceLoader propertySourceLoader(){
        return new PropertiesPropertySourceLoader();
    }

    static class MyPropertiesPropertySourceLoader extends PropertiesPropertySourceLoader{

        @Override
        public String[] getFileExtensions() {
            return super.getFileExtensions();
        }

        @Override
        public List<org.springframework.core.env.PropertySource<?>> load(String name, Resource resource) throws IOException {
            var enr=new EncodedResource(resource,"utf-8");
            var lll= PropertiesLoaderUtils.loadProperties(enr);
            return super.load(name, resource);
        }
    }
}
