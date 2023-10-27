package io.github.eeroom.springcore.ITCast01_国际化;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class RootConfig {

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
