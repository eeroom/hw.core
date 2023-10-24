package io.github.eeroom.springboot2xcore.启动参数和配置文件;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@SpringBootApplication
@PropertySource(value = {"myconfig.properties"},encoding = "UTF-8")
public class App {

    public static void main(String[] args) throws IOException {
        String DEFAULT_SEARCH_LOCATIONS = "classpath:/,classpath:/config/,file:./,file:./config/*/,file:./config/";
        /**
         * 1、PropertySource引入的配置文件只会根据配置的地址读取内容，value = {"myconfig.properties"}等价于 value = {"classpath:myconfig.properties"}
         * 2、虽然已经配置PropertySource，但总是会读取application.properties文件中的内容
         * 2、springboot读取application.properties文件的顺序如下
         *      classpath:/application.properties
         *      classpath:/config/application.properties
         *      file:./
         *      file:./config/
         * 后读取的配置文件设置会覆盖之前读取的配置值
         * 所以：file:./config/ 路径下的配置文件会覆盖其他配置的值
         * 特别的：file:的取值就是cmd启动jar文件时的当前目录，
         *          也就是说，cmd的当前工作目录下的config目录中application。properties才会被读取
         *              和jar同目录的config的配置被读取的前提条件：cmd的工作目录为 xxx.jar文件所在的目录
         * 启动参数中的值会覆盖配置文件中同名的值
         * java -Dmsg=abc -jar xxxx.jar --msg=tgh
         * --的参数会覆盖-D的值
         */
        var springApplication= new SpringApplication(App.class);
        var context= springApplication.run(args);
        var ccg= context.getEnvironment().getProperty("ccg");
        System.out.println("myconfig。ccg:"+ccg);
        var msg= context.getEnvironment().getProperty("msg");
        System.out.println("msg:"+msg);


        System.in.read();
        int a=3;
    }
}
