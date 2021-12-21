package org.azeroth.springcontext;

import org.springframework.stereotype.Component;

@Component
public class ChinaSuanfa implements ISuanfa {
    @Override
    public int Hit(int a, int b) {
        System.out.println("a+b=");
        System.out.print(a+b);
        return a+b;
    }
}
