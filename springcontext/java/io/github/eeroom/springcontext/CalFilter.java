package io.github.eeroom.springcontext;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CalFilter {

    @Around("execution (public * Calculation.*(..))")
    public Object doHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("执行方法前");
        var rt= joinPoint.proceed(joinPoint.getArgs());
        System.out.println("执行方法后");
        return rt;
    }
}
