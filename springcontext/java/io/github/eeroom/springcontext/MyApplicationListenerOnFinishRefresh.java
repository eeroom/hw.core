package io.github.eeroom.springcontext;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class MyApplicationListenerOnFinishRefresh implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //context的refresh已经完成,可以方便从cotnext获取bean；
        //使用场景，ssm中，webinitializer的实现类子类中，容器的创建和refresh都在springmvc代码中，
        //配置servlet上传功能，springmvc提供的void customizeRegistration(ServletRegistration.Dynamic registration)方法中，拿不到容器，而且就算通过反射强行拿到容器，容器在这个时间点还没有refresh,而且我们也不能直接对其调用refresh,否者springmvc后面的代码要报错
        //这个时候利用ApplicationContextInitializer和ApplicationListener，注册ContextRefreshedEvent事件的回调方法，然后在这个方法里面利用容器读取配置信息，然后配置上传功能
        System.out.println("执行事件ContextRefreshedEvent的回调方法");
    }
}
