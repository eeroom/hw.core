package io.github.eeroom.springcore.Day01BeanFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        var beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("student", BeanDefinitionBuilder.genericBeanDefinition(Student.class)
                .setScope(BeanDefinition.SCOPE_PROTOTYPE).getBeanDefinition());
        var student1= beanFactory.getBean(Student.class);
        var student2=beanFactory.getBean(Student.class);
        System.out.println("student1==student2："+(student1==student2));

    }
}
