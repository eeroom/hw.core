package io.github.eeroom.springmvc.authen;

import io.github.eeroom.springmvc.model.UserInfoWrapper;
import org.springframework.beans.BeanInfoFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 认证的filter
 */
@Component("authenticationFilter")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AuthenticationFilter implements Filter, ApplicationContextAware, ServletConfigAware {
    FilterConfig filterConfig;
    ApplicationContext applicationContext;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig=filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //取到ioc容器
        var uw= this.applicationContext.getBean(UserInfoWrapper.class);
        uw.setName("userName");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
        try {
            this.init(new FilterConfig(){

                @Override
                public String getFilterName() {
                    return null;
                }

                @Override
                public ServletContext getServletContext() {

                    return null;
                }

                @Override
                public String getInitParameter(String s) {
                    return null;
                }

                @Override
                public Enumeration<String> getInitParameterNames() {
                    return null;
                }
            });
        } catch (ServletException e) {
            throw new RuntimeException("",e);
        }
    }
    ServletConfig servletConfig;
    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig=servletConfig;
    }
}
