package azeroth.tbc.yxspring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan
public class RootConfig {

    @Bean
    public ISuanfa suanfa(){
        return  new ChinaSuanfa();
    }

    @Bean
    public Calculation calculation(){
        return new Calculation(this.suanfa());
    }
}
