package io.github.eeroom.springcore.H01ApplicationContext和BeanFactory的关系;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.support.GenericApplicationContext;

public class 事件体系 {

    public static void main(String[] args) {

        var context = new GenericApplicationContext();
        var bd= BeanDefinitionBuilder.genericBeanDefinition(Person.class)
                .getBeanDefinition();
        context.registerBeanDefinition(Person.class.getName(),bd);
        context.addApplicationListener(new ApplicationListener<PersonGoHomeEvent>() {
            @Override
            public void onApplicationEvent(PersonGoHomeEvent applicationEvent) {
                System.out.println("onApplicationEvent");
                System.out.println(applicationEvent.getMsg());
            }
        });
        context.refresh();
        var bean= context.getBean(Person.class);
        bean.gohome();


    }

    static class Person implements ApplicationContextAware {

        ApplicationContext applicationContext;

        void gohome() {
            System.out.println("执行Person::gohome");
            var event=new PersonGoHomeEvent(this.applicationContext);
            event.setMsg("我回来了");
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
