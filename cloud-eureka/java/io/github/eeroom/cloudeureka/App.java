package io.github.eeroom.cloudeureka;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.text.MessageFormat;

@SpringBootApplication
@EnableEurekaServer
public class App {
    public static void main(String[] args){
        var context= new SpringApplicationBuilder(App.class).web(true).run(args);
        System.out.println(MessageFormat.format("访问地址：http://{0}:{1}",
                context.getEnvironment().getProperty("eureka.instance.hostname"),
                context.getEnvironment().getProperty("server.port")));
    }
}
