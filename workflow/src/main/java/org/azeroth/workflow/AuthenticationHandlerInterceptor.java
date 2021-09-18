package org.azeroth.workflow;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
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
            return this.noLogin(httpServletResponse);
        var jwtVerifier= com.auth0.jwt.JWT.require(Algorithm.HMAC256("hw@123456"))
                .withIssuer("workflow")
                .build();
        var jwtDecode= jwtVerifier.verify(jwtstring);
        var userName= jwtDecode.getClaim("userName").asString();
        if(userName==null ||userName.length()<=0)
            return this.noLogin(httpServletResponse);
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

    private boolean noLogin(HttpServletResponse httpServletResponse) throws Exception {
        var apiresult=new ApiResultWrapper();
        apiresult.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        apiresult.setMessage("请登陆");
        com.fasterxml.jackson.databind.ObjectMapper objectMapper=new ObjectMapper();
        //反序列的时候，如果json中有额外的字段，class没有与其对应的属性，默认报错，这样配置就不会报错，
        //这个配置对于调用第三方接口特别重要，接口可能增加某个字段数据，但我们不需要用，也就没有修改代码
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        var rtbuffer= objectMapper.writeValueAsBytes(apiresult);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.getOutputStream().write(rtbuffer);
        return false;
    }

}
