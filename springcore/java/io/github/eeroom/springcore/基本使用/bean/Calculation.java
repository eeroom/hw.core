package io.github.eeroom.springcore.基本使用.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 基于自动扫包注册bean
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Calculation {

    //属性或字段注入
    @Value("${mqs.url}")
    String url;

    ISuanfa sf;

    //构造函数注入
    @Autowired
    public Calculation(ISuanfa sf){
        this.sf=sf;
    }

    public void  Handler(){
        System.out.println("配置文件获取的mqs.url="+this.url);
        this.sf.Hit(1,10);
    }
}
