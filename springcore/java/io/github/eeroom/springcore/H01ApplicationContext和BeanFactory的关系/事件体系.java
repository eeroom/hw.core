package io.github.eeroom.springcore.H01ApplicationContext和BeanFactory的关系;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.GenericApplicationContext;

public class 事件体系 {

    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext();
        context.register(Config.class);
        var bd = BeanDefinitionBuilder.genericBeanDefinition(Person.class)
                .getBeanDefinition();
        context.registerBeanDefinition(Person.class.getName(), bd);
        context.registerBeanDefinition(Dog.class.getName(), BeanDefinitionBuilder.genericBeanDefinition(Dog.class).getBeanDefinition());
        context.addApplicationListener(new ApplicationListener<PersonGoHomeEvent>() {
            @Override
            public void onApplicationEvent(PersonGoHomeEvent applicationEvent) {
                System.out.println("addApplicationListener");
                System.out.println(applicationEvent.getMsg());
            }
        });
        context.refresh();
        var bean = context.getBean(Person.class);
        bean.gohome();


    }

    @Configuration
    static class Config {

//        @Bean("ApplicationEventMulticaster")
//        ApplicationEventMulticaster applicationEventMulticaster(){
//            var pulisher=new SimpleApplicationEventMulticaster();
//            pulisher.setTaskExecutor(new ex);
//        }
    }

    static class Dog {

        @EventListener
        void say(PersonGoHomeEvent event) {
            System.out.println("Dog::say");
            System.out.println(event.getMsg());
        }
    }


    static class Person implements ApplicationContextAware {

        ApplicationContext applicationContext;

        void gohome() {
            System.out.println("执行Person::gohome");
            var event = new PersonGoHomeEvent(this.applicationContext);
            event.setMsg("主人回家了");
            this.applicationContext.publishEvent(event);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

    }

    static class PersonGoHomeEvent extends ApplicationContextEvent {
        String msg;

        public PersonGoHomeEvent(ApplicationContext source) {
            super(source);
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
