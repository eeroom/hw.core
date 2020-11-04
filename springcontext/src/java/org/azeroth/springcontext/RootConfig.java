package org.azeroth.springcontext;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan
@PropertySource(value = {"classpath:application.properties"})
public class RootConfig {

//    @Bean
//    public ISuanfa suanfa(){
//        return  new ChinaSuanfa();
//    }
//
//    @Bean
//    public Calculation calculation(){
//        return new Calculation(this.suanfa());
//    }
}
