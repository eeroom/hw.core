package io.github.eeroom.springcore.ITCast01_国际化;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Locale;

public class App {

    public static void main(String[] args){
        /**
         * 默认不支持从配置文件读取多语言信息
         * 预留扩展点，优先获取并设置 beanName为 messageSource 的对象处理多语言信息
         * 参看refresh方法中的 initMessageSource() 部分
         */
        var context=new AnnotationConfigApplicationContext();
        context.register(RootConfig.class);
        context.refresh();
        System.out.println("hi:"+context.getMessage("hi",null, Locale.CHINA));
        System.out.println("hi:"+context.getMessage("hi",null, Locale.JAPAN));
        System.out.println("ai:"+context.getMessage("ai",null, Locale.JAPAN));
    }


}
