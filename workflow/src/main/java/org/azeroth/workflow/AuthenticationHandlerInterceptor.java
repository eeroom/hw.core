package org.azeroth.workflow;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(!(o instanceof HandlerMethod))
            return true;
        var method=(HandlerMethod)o;
        if(method.getMethod().getDeclaringClass().getPackageName().indexOf("workflow.controller")<0)
            return true;
        if(method.getMethodAnnotation(SkipAuthentication.class)!=null)
            return true;
        var jwtstring= httpServletRequest.getHeader("Authorization");
        if(jwtstring==null || jwtstring.length()<=0)
            return false;
        var jwtVerifier= com.auth0.jwt.JWT.require(Algorithm.HMAC256("hw@123456"))
                .withIssuer("workflow")
                .build();
        var jwtDecode= jwtVerifier.verify(jwtstring);
        var userName= jwtDecode.getClaim("userName").asString();
        if(userName==null ||userName.length()<=0)
            return false;
        var context= WebApplicationContextUtils.getWebApplicationContext(httpServletRequest.getServletContext());
        var loginUserInfo=context.getBean(LoginUserInfo.class);
        loginUserInfo.setName(userName);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
