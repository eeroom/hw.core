package org.azeroth.springmvc;

import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;

@org.springframework.context.annotation.Configuration
@org.springframework.web.servlet.config.annotation.EnableWebMvc
@org.springframework.context.annotation.ComponentScan
public class WebConfig extends org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter {
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
