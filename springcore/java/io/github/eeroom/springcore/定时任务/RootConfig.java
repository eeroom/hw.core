package io.github.eeroom.springcore.定时任务;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class RootConfig {

//    @Bean(name = "scheduledExecutorService")
//    public ScheduledExecutorService scheduledExecutorService(){
//        var scheduledExecutorService= Executors.newScheduledThreadPool(4);
//        return scheduledExecutorService;
//    }
}
