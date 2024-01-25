package io.github.eeroom.cloudconfig;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class App {
    public static void main(String[] args) {
        var context = SpringApplication.run(App.class, args);
        var password = context.getEnvironment().getProperty("spring.cloud.config.server.git.password");
        System.out.println("当前密码：" + password);
    }
}
