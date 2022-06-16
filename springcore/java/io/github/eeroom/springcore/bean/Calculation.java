package io.github.eeroom.springcore.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Calculation {

    @Value("${mqs.url}")
    String url;

    ISuanfa sf;

    @Autowired
    public Calculation(ISuanfa sf){
        this.sf=sf;
    }

    public void  Handler(){
        System.out.println("配置文件获取的mqs.url="+this.url);
        this.sf.Hit(1,10);
    }
}
