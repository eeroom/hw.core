package io.github.eeroom.springcore.bean;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChinaSuanfa implements ISuanfa {
    @Override
    public int Hit(int a, int b) {
        System.out.print("ChinaSuanfa:a+b=");
        System.out.println(a+b);
        return a+b;
    }
}
