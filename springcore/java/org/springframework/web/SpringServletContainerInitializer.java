package org.springframework.web;

import javax.servlet.ServletContainerInitializer;

public class SpringServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Object servletcontext) {
        System.out.println("java-spi,SpringServletContainerInitializer的实例执行onStartup");
    }
}
