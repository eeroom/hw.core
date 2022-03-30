package io.github.eeroom.springcontext;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChinaSuanfa implements ISuanfa {
    @Override
    public int Hit(int a, int b) {
        System.out.println("a+b=");
        System.out.print(a+b);
        return a+b;
    }
}
