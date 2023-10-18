package io.github.eeroom.springcore.基本使用.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 基于自动扫包注册bean
 */
@Component("chinasuangfa")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChinaSuanfa implements ISuanfa {
    @Autowired
    String seyhello;

    @Autowired
    @Qualifier("seyhello")
    String seyhello2;

    @Override
    public int Hit(int a, int b) {
        System.out.println(seyhello);
        System.out.print("ChinaSuanfa:a+b=");
        System.out.println(a+b);
        return a+b;
    }
}
