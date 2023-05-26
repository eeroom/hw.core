package io.github.eeroom.springcore;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.beans.PropertyDescriptor;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ITCast04BeanFactory02 {
    public static void main(String[] args){
        var lstpp= BeanUtils.getPropertyDescriptors(LifeCycleBean.class);
        var context=new AnnotationConfigApplicationContext();
        context.registerBeanDefinition("lifeCycleBean", BeanDefinitionBuilder.genericBeanDefinition(LifeCycleBean.class)
                .setScope(BeanDefinition.SCOPE_PROTOTYPE)
                .getBeanDefinition());
        context.registerBeanDefinition("myBeanPostProcessor",BeanDefinitionBuilder.genericBeanDefinition(MyBeanPostProcessor.class).getBeanDefinition());
        context.refresh();
//        var autowiredAnnotationBeanPostProcessor= context.getBean(AutowiredAnnotationBeanPostProcessor.class);
//        var lst=new HashSet<Class<? extends Annotation>>();
//        lst.add(Autowired.class);
//        lst.add(Value.class);
//        lst.add(Abc.class);
//        autowiredAnnotationBeanPostProcessor.setAutowiredAnnotationTypes(lst);
        var lifeCycleBean= context.getBean("lifeCycleBean");
        context.close();
        System.out.println("lifeCycleBean:"+lifeCycleBean);

    }

    @Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Abc {
        int max() default 3;
    }

    static class  LifeCycleBean{
        LifeCycleBean(){
            System.out.println("构造");
        }

        @Autowired
        public void setHome(@Value("${JAVA_HOME}") String home){
            System.out.println("依赖注入setHome："+home);
        }

        @Abc
        public void setHead(int va){
            System.out.println("依赖注入myHead："+va);
        }

        @PostConstruct
        public void  init(){
            System.out.println("初始化init");
        }

        @PreDestroy
        public  void  destroy(){
            System.out.println("销毁destroy");
        }
    }

    static class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor, PriorityOrdered {


        @Override
        public void postProcessBeforeDestruction(Object o, String beanName) throws BeansException {
            if(beanName.equals("lifeCycleBean")){
                System.out.println("销毁之前执行：postProcessBeforeDestruction");
            }
        }

        @Override
        public boolean requiresDestruction(Object o) {
            return true;
        }

        @Override
        public Object postProcessBeforeInstantiation(Class<?> aClass, String beanName) throws BeansException {
            if(beanName.equals("lifeCycleBean")){
                System.out.println("实例化 之前 执行：postProcessBeforeInstantiation，如果这里返回的不为null,则返回值就是后续的bean实例");
            }
            return null;
        }

        @Override
        public boolean postProcessAfterInstantiation(Object o, String beanName) throws BeansException {
            if(beanName.equals("lifeCycleBean")){
                System.out.println("实例化 之后 执行：postProcessAfterInstantiation，返回值为false,就会跳过依赖注入");
            }
            return true;
        }

        @Override
        public PropertyValues postProcessPropertyValues(PropertyValues propertyValues, PropertyDescriptor[] propertyDescriptors, Object o, String beanName) throws BeansException {
            if(beanName.equals("lifeCycleBean")){
                System.out.println("依赖注入阶段执行：postProcessPropertyValues，@Autowired @Value  @Resource");
            }
            this.dictInjectMethod.get(beanName).forEach(x->{
                var abc= AnnotationUtils.getAnnotation(x,Abc.class);
                var value= abc.max()*6;
                try {
                    x.invoke(o,value);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });

            return propertyValues;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if(beanName.equals("lifeCycleBean")){
                System.out.println("初始化 之前 执行，返回值会替换掉原本的bean实例：postProcessBeforeInitialization，@PostConstruct @ConfigurationProperties");
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if(beanName.equals("lifeCycleBean")){
                System.out.println("初始化 之后 执行，返回值会替换掉原本的bean实例：postProcessAfterInitialization，使用场景：代理增强");
            }
            return bean;
        }

        HashMap<String,List<Method>> dictInjectMethod=new HashMap<>();

        @Override
        public void postProcessMergedBeanDefinition(RootBeanDefinition rootBeanDefinition, Class<?> aClass, String beanName) {
            if(beanName.equals("lifeCycleBean")){
                System.out.println("postProcessMergedBeanDefinition");
            }
            this.dictInjectMethod.put(beanName,new ArrayList<Method>());
            ReflectionUtils.doWithLocalMethods(aClass,method -> {
                var abc = AnnotatedElementUtils.getMergedAnnotation(method, Abc.class);
                if(abc!=null){
                    this.dictInjectMethod.get(beanName).add(method);
                }
            });

        }

        @Override
        public int getOrder() {
            return 2147483645-2;
        }
    }
}
