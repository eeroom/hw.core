package io.github.eeroom.springcore.定时任务;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope
@Aspect
public class Filter {

    @Around("execution (public * io.github.eeroom.springcore.定时任务.Task.*(..))")
    public Object doHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("spring-aop:执行方法前");
        var rt= joinPoint.proceed(joinPoint.getArgs());
        System.out.println("spring-aop:执行方法后");
        return rt;
    }
}
