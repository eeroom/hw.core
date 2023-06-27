package io.github.eeroom.springcore.H01ApplicationContext和BeanFactory的关系;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class 事件体系 {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext();
        context.registerBeanDefinition(Dog.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(Dog.class).getBeanDefinition());
        /**
         * 默认的时间发布器就是 SimpleApplicationEventMulticaster，但是没有使用线程池
         * 预留了扩展点，优先获取并设置 beanName为 applicationEventMulticaster 的对象为事件发布器
         * 参看refresh方法中的 initApplicationEventMulticaster() 部分
         * 为时间发布器设置一个线程池，则 变为异步回调事件监听函数
         */
        var executor=new ThreadPoolTaskExecutor();
        executor.initialize();
        context.registerBeanDefinition("applicationEventMulticaster2",
                BeanDefinitionBuilder.genericBeanDefinition(SimpleApplicationEventMulticaster.class)
                        .addPropertyValue("taskExecutor",executor)
                        .setScope(BeanDefinition.SCOPE_SINGLETON)
                        .getBeanDefinition());

        context.addApplicationListener((MyEvent event)->{
            System.out.println("执行通过addApplicationListener添加的事件监听函数，消息内容："+event.getMsg());
        });
        context.addApplicationListener((ApplicationContextEvent event)->{
            System.out.println("事件内容："+event);
        });
        context.refresh();
        /**
         * 触发 MyEvent 时间
         */
        context.publishEvent(new MyEvent(context,"我出发了！"));
        /**
         * 会出发内置的一些事件
         */
        context.start();
        context.stop();
        context.close();
    }

    static class Dog {

        @EventListener
        void say(MyEvent event) {
            System.out.println("执行通过注解添加的事件监听函数,消息内容："+event.getMsg());
        }
    }

    static class MyEvent extends ApplicationContextEvent {
        String msg;

        public MyEvent(ApplicationContext source,String msg) {
            super(source);
            this.msg=msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
