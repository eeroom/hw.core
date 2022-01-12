package io.github.eeroom.hzoa.authen;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.eeroom.hzcore.ApidataWrapper;
import io.github.eeroom.hzoa.AppConfig;
import io.github.eeroom.hzoa.MyObjectFacotry;
import org.springframework.http.MediaType;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(!(o instanceof HandlerMethod))
            return true;
        var method=(HandlerMethod)o;
        var appconfig= MyObjectFacotry.getBean(AppConfig.class);
        if(method.getMethod().getDeclaringClass().getPackageName().indexOf(appconfig.controllerPath)<0)
            return true;
        if(method.getMethodAnnotation(SkipAuthentication.class)!=null)
            return true;
        var jwtstring= httpServletRequest.getHeader(appconfig.authenJwtHeader);
        if(jwtstring==null || jwtstring.length()<=0)
            return this.processInvalidToken(httpServletResponse);
        var account= JwtTokenHelper.decode(jwtstring);
        if(account==null ||account.length()<=0)
            return this.processInvalidToken(httpServletResponse);
        var context= WebApplicationContextUtils.getWebApplicationContext(httpServletRequest.getServletContext());
        var currentUserInfo=context.getBean(CurrentUserInfo.class);
        currentUserInfo.setAccount(account);
        //需要根据account完善用户信息，比如从数据库取数据
        currentUserInfo.setName(account);
        currentUserInfo.setId(account);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    boolean processInvalidToken(HttpServletResponse httpServletResponse) throws Exception {
        var apiresult=new ApidataWrapper();
        apiresult.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        apiresult.setMessage("未登陆或者登陆已过期");
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
