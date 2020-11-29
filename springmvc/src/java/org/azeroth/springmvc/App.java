package org.azeroth.springmvc;

import org.azeroth.springmvc.springsecurity.SpringSecurityConfig;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

public class App extends  org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class, SpringSecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {

        //开启spring-security，实现自己的登陆校验，把springsecurity相关的先不加载
//        var filter=new org.springframework.web.filter.DelegatingFilterProxy("springSecurityFilterChain");
//        return new Filter[]{filter};
        //结束spring-security
        return new Filter[0];
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        //开启支持multipart/form-data，这样才能上传文件
        registration.setMultipartConfig(new MultipartConfigElement("D:/springmvc_multipart_formdata",2097152,4194304,0));
    }
}
