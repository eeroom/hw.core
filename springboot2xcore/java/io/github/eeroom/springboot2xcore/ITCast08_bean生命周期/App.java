package io.github.eeroom.springboot2xcore.ITCast08_bean生命周期;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class App {

    public static void main(String[] args){

        /**
         * 打开浏览器 http://localhost:8080/home/say
         * 相同选项卡，可以观察每次刷新的结果 Session的不会变，Request会变
         * 相同浏览器不同选项卡，可以观察每次刷新的结果 Session和隔壁选项卡的相同，Request会变
         * 打开不同浏览器，可以观察每次刷新的结果 Session和隔壁浏览器的不同，Request会变
         * 修改servlet的失效时间为10s,但是tomcat内部对session的失效检测内部有一个最小刷新周期，大概1分钟
         * 等待一段时间，可以在idea的运行结果窗口查看销毁情况，request的每次都会销毁，session需要等待一段时间
         */
        var context= SpringApplication.run(App.class);
    }
}
