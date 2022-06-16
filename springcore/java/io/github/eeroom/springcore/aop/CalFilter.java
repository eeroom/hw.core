package io.github.eeroom.springcore.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Aspect
public class CalFilter {

    @Around("execution (public * io.github.eeroom.springcontext.bean.Calculation.*(..))")
    public Object doHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("spring-aop:执行方法前");
        var rt= joinPoint.proceed(joinPoint.getArgs());
        System.out.println("spring-aop:执行方法后");
        return rt;
    }
}
