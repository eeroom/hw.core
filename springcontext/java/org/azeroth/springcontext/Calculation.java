package org.azeroth.springcontext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Calculation {

    @Value("${mqs.url}")
    String url;

    ISuanfa sf;

    @Autowired
    public Calculation(ISuanfa sf){
        this.sf=sf;
    }

    public void  Handler(){
        System.out.println(this.url);
        this.sf.Hit(1,10);
    }
}
