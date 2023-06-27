package io.github.eeroom.springcore.H01ApplicationContext和BeanFactory的关系;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class 国际化 {

    public static void main(String[] args){
        /**
         * 默认不支持从配置文件读取多语言信息
         * 预留扩展点，优先获取并设置 beanName为 messageSource 的对象处理多语言信息
         * 参看refresh方法中的initMessageSource()部分
         */
        var context=new AnnotationConfigApplicationContext();
        context.register(Config.class);
        context.refresh();
        System.out.println("hi:"+context.getMessage("hi",null, Locale.CHINA));
        System.out.println("hi:"+context.getMessage("hi",null, Locale.JAPAN));
        System.out.println("ai:"+context.getMessage("ai",null, Locale.JAPAN));
    }

    @Configuration
    static class Config{

        @Bean
        public MessageSource messageSource(){
            var messageSource=new ResourceBundleMessageSource();
            messageSource.setDefaultEncoding("utf-8");
            //指定多语言的配置文件，对应 messages.properties messages_xx.properties
            //此处设置等价于 classpath:messages
            //如果把配置文件打包到jar外面，则使用 file:d:/国际化/messages
            messageSource.setBasenames(new String[]{"messages","d:/国际化/messages"} );
            //默认情况下：如果指定的key不存在，就报错
            // 设置为true,则不报错，直接返回key
            messageSource.setUseCodeAsDefaultMessage(true);
            //默认情况下：如果指定的key在指定的语言文件中不存在，就从当前系统对应的语言文件中去找，当前是中文windows，则从messages_zh.properties中去找
            //设置为false，则从messages.properties中去找
            messageSource.setFallbackToSystemLocale(false);
            return messageSource;
        }
    }
}
