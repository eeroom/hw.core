package io.github.eeroom.springcore.定时任务;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class Task implements ApplicationContextAware {

    @Scheduled(cron = "0/3 * * * * ?")
    public void start(){
        System.out.println("开始定时任务");
        this.applicationContext.getBean(Task.class).hook();
        //this.hook();

    }

    public void hook(){
        System.out.println("hook");
    }
    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
