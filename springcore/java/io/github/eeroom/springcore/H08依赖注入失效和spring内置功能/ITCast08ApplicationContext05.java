package io.github.eeroom.springcore.H08依赖注入失效和spring内置功能;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class ITCast08ApplicationContext05 {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext();
        context.register(MyConfig.class);
        context.refresh();
        var bd= (AbstractBeanDefinition)context.getBeanDefinition("myBean");
        System.out.println("@Bean(initMethod = \"start\"),最终被添加到对应的beandefination中，由beanfactory内置调用");
        System.out.println("myBean.getInitMethodName="+bd.getInitMethodName());
        System.out.println("@Bean(destroyMethod = \"end2\"),最终被添加到对应的beandefination中，由beanfactory内置调用");
        System.out.println("myBean.getInitMethodName="+bd.getDestroyMethodName());
        context.close();

    }

    @Configuration
    static class MyConfig {

        /**
         * 这个initMethod = "start"最终被添加到对应的beandefination中，由beanfactory内置调用
         * @return
         */
        @Bean(initMethod = "start",destroyMethod = "end2")
        public MyBean myBean() {
            return new MyBean();
        }
    }

    static class MyBean implements InitializingBean, DisposableBean {

        public void start() {
            System.out.println("initMethod   start");
        }

        /**
         * 由beanfactory内置调用
         * @throws Exception
         */
        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("InitializingBean ");
        }

        /**
         * 这个由CommonAnnotationBeanPostProcessor后处理调用
         */
        @PostConstruct
        public void start2() {
            System.out.println("@PostConstruct");
        }

        @Override
        public void destroy() throws Exception {
            System.out.println("DisposableBean ");
        }

        /**
         * 这个由CommonAnnotationBeanPostProcessor后处理调用
         */
        @PreDestroy
        public void end(){
            System.out.println("@PreDestroy");
        }

        public void  end2(){
            System.out.println("destroyMethod   end2");
        }
    }
}
