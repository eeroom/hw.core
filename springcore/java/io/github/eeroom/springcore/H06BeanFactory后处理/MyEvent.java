package io.github.eeroom.springcore.H06BeanFactory后处理;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class MyEvent extends ApplicationContextEvent {
    public MyEvent(ApplicationContext source) {
        super(source);
    }

    public Object tag;
}
