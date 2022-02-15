package io.github.eeroom.springcontext;

import org.springframework.context.ApplicationListener;

public class MyApplicationListenerOnMyEvent implements ApplicationListener<MyEvent> {
    @Override
    public void onApplicationEvent(MyEvent myEvent) {
        System.out.println("执行事件MyEvent的回调方法");
    }
}
