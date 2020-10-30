package org.azeroth.springmvc;

import javax.servlet.Filter;

public class App extends  org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class,SpringSecurityConfig.class};
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
        //开启spring-security
        var filter=new org.springframework.web.filter.DelegatingFilterProxy("springSecurityFilterChain");
        return new Filter[]{filter};
    }
}
