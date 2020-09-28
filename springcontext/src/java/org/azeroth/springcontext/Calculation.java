package org.azeroth.springcontext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Calculation {

    ISuanfa sf;
    @Autowired
    public Calculation(ISuanfa sf){
        this.sf=sf;
    }

    public void  Handler(){
        this.sf.Hit(1,10);
    }
}
