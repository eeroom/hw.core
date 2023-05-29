package io.github.eeroom.springcore;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ITCast05BeanFactory03 {
    public static void main(String[] args) throws Throwable {
        var context = new GenericApplicationContext();
        context.registerBeanDefinition("lifeCycleBean", BeanDefinitionBuilder.genericBeanDefinition(LifeCycleBean.class)
                .setScope(BeanDefinition.SCOPE_PROTOTYPE)
                .getBeanDefinition());
        //解析@autowired @value 在依赖注入阶段执行
        context.registerBeanDefinition(AutowiredAnnotationBeanPostProcessor.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(AutowiredAnnotationBeanPostProcessor.class).getBeanDefinition());
        System.out.println(context.getDefaultListableBeanFactory().getAutowireCandidateResolver());
        //解析 @autowired 依赖注入的时候 对@value标注的字段或参数进行进行解析赋值，默认 是SimpleAutowireCandidateResolver 不能解析@Value("${JAVA_HOME}")
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        //解析 @PostConstruct @PostConstruct
        //容器只负责单例的销毁，多例的bean忽略，应该是由jvm的自动垃圾回收处理
        context.registerBeanDefinition(CommonAnnotationBeanPostProcessor.class.getName(),
                BeanDefinitionBuilder.genericBeanDefinition(CommonAnnotationBeanPostProcessor.class).getBeanDefinition());
        //context.registerBeanDefinition("myBeanPostProcessor", BeanDefinitionBuilder.genericBeanDefinition(ITCast04BeanFactory02.MyBeanPostProcessor.class).getBeanDefinition());
        context.refresh();
        var lifeCycleBean = context.getBean("lifeCycleBean");
        context.close();
        System.out.println("lifeCycleBean:" + lifeCycleBean);


        System.out.println("-----------AutowiredAnnotationBeanPostProcessor--原理----");
        var beanFactory=new DefaultListableBeanFactory();
        //支持@Value解析
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        //支持${}占位符解析
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);
        var autowiredAnnotationBeanPostProcessor=new AutowiredAnnotationBeanPostProcessor();
        autowiredAnnotationBeanPostProcessor.setBeanFactory(beanFactory);
        var lb=new LifeCycleBean();
        var lstpp = BeanUtils.getPropertyDescriptors(LifeCycleBean.class);//可选，为空不影响依赖注入
        autowiredAnnotationBeanPostProcessor.postProcessPropertyValues(null,lstpp,lb,"lb");

        /**
         * 注入的核心逻辑
         * 根据@Autowire扫描对应的字段和方法
         * doResolveDependency()获取被注入的字段值或者方法参数值
         * 对要执行依赖注入的字段或者方法执行invoke
         */
        System.out.println("-----------AutowiredAnnotationBeanPostProcessor--注入的原理----");
        var setHome= LifeCycleBean.class.getDeclaredMethod("setHome",String.class);
        var setHomeParameter0=new MethodParameter(setHome,0);
        var setHomeParameter0Dp=new DependencyDescriptor(setHomeParameter0,true);
        var p0Value= beanFactory.doResolveDependency(setHomeParameter0Dp,null,null,null);
        System.out.println("setHomeParameter0Value=" + p0Value);
        setHome.invoke(new LifeCycleBean(),p0Value);



    }

    static class LifeCycleBean implements InitializingBean {
        LifeCycleBean() {
            System.out.println("实例化 执行构造函数");
        }

        @Autowired
        public void setHome(@Value("${JAVA_HOME}") String home) {
            System.out.println("依赖注入setHome：" + home);
        }

        @Resource
        public void setHead(@Value("${JAVA_HOME}") String home) {
            System.out.println("依赖注入myHead：" + home);
        }

        //这个注解在jakarta.annotation-api依赖中定义，还包括：@PreDestroy @Resource
        @PostConstruct
        public void init() {
            System.out.println("初始化 之前 执行@PostConstruct标注的方法");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("销毁destroy");
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("InitializingBean::afterPropertiesSet");
        }
    }

    /**
     * bean的生命周期和bean的后处理器
     * 自定义依赖注入的注解和注入逻辑
     */
    static class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor, PriorityOrdered {


        @Override
        public void postProcessBeforeDestruction(Object o, String beanName) throws BeansException {
            if (beanName.equals("lifeCycleBean")) {
                System.out.println("销毁之前执行：postProcessBeforeDestruction");
            }
        }

        @Override
        public boolean requiresDestruction(Object o) {
            return true;
        }

        @Override
        public Object postProcessBeforeInstantiation(Class<?> aClass, String beanName) throws BeansException {
            if (beanName.equals("lifeCycleBean")) {
                System.out.println("实例化 之前 执行：postProcessBeforeInstantiation，如果这里返回的不为null,则返回值就是后续的bean实例");
            }
            return null;
        }

        @Override
        public boolean postProcessAfterInstantiation(Object o, String beanName) throws BeansException {
            if (beanName.equals("lifeCycleBean")) {
                System.out.println("实例化 之后 执行：postProcessAfterInstantiation，返回值为false,就会跳过依赖注入");
            }
            return true;
        }

        @Override
        public PropertyValues postProcessPropertyValues(PropertyValues propertyValues, PropertyDescriptor[] propertyDescriptors, Object o, String beanName) throws BeansException {
            if (beanName.equals("lifeCycleBean")) {
                System.out.println("依赖注入阶 执行：postProcessPropertyValues，@Autowired @Value  @Resource");
            }
            return propertyValues;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (beanName.equals("lifeCycleBean")) {
                System.out.println("初始化 之前 执行，返回值会替换掉原本的bean实例：postProcessBeforeInitialization，@PostConstruct @ConfigurationProperties");
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (beanName.equals("lifeCycleBean")) {
                System.out.println("初始化 之后 执行，返回值会替换掉原本的bean实例：postProcessAfterInitialization，使用场景：代理增强");
            }
            return bean;
        }

        HashMap<String, List<Method>> dictInjectMethod = new HashMap<>();

        @Override
        public void postProcessMergedBeanDefinition(RootBeanDefinition rootBeanDefinition, Class<?> aClass, String beanName) {
            if (beanName.equals("lifeCycleBean")) {
                System.out.println("postProcessMergedBeanDefinition");
            }
        }

        /**
         * 容器会按order排序，然后执行各个后处理器
         * 处理@Autowired @Value注入的后处理器的order是2147483645，值越大，优先级越低
         *
         * @return
         */
        @Override
        public int getOrder() {
            return 2147483645 - 2;
        }
    }
}
