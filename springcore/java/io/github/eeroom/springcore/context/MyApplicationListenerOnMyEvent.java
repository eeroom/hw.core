package io.github.eeroom.springcore.context;

import org.springframework.context.ApplicationListener;

public class MyApplicationListenerOnMyEvent implements ApplicationListener<MyEvent> {
    @Override
    public void onApplicationEvent(MyEvent myEvent) {
        System.out.println("spring-event:自定义事件MyEvent的回调方法");
    }
}
