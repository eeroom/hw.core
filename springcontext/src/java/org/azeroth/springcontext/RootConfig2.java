package org.azeroth.springcontext;

import org.springframework.context.annotation.Bean;

public class RootConfig2 {

    @Bean
    public String seyhello(){
        return "sayhello";
    }
}
