package org.azeroth.springcontext;

import org.springframework.context.annotation.Bean;

/**
 * 配置MyImportSelector，向容器注册bean
 */
public class RootConfig2 {

    @Bean
    public String seyhello(){
        return "sayhello";
    }
}
