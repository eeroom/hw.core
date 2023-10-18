package io.github.eeroom.springcore.H06BeanFactory后处理;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableWCH
@Configuration
public class RootConfig3 {

    @Bean
    public String sk(){
        return "sk";
    }

}
