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

    @Autowired
    ISuanfa taskService4Target;

    @Autowired
    ISuanfa taskService4Self;

    @Autowired
    ISuanfa chinasuangfa;
    //构造函数注入


    public void  Handler(){
        System.out.println("配置文件获取的mqs.url="+this.url);
        var rt= this.chinasuangfa.Hit(1,10);
        var rt2=this.taskService4Self.Hit(1,1);
        var rt3=this.taskService4Target.Hit(1,1);
        int aa=5;

    }
}
