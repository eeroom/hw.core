package io.github.eeroom.springboot2xcore.配置文件;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:myconfig.properties"},encoding = "UTF-8")
public class App {

    public static void main(String[] args){
        var springApplication= new SpringApplication(App.class);
        var context= springApplication.run(args);
        var port= context.getEnvironment().getProperty("server.port");
        //虽然已经配置PropertySource，但总是会读取application.properties文件中的内容
        var msg= context.getEnvironment().getProperty("msg");
        int a=3;


    }
}
