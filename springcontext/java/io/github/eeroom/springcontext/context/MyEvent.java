package io.github.eeroom.springcontext.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class MyEvent extends ApplicationContextEvent {
    public MyEvent(ApplicationContext source) {
        super(source);
    }

    public Object tag;
}
