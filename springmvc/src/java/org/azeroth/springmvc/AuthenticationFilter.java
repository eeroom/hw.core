package org.azeroth.springmvc;

import org.azeroth.springmvc.model.UserInfoWrapper;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import java.io.IOException;

/**
 * 认证的filter
 */
public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //取到ioc容器
        var context= WebApplicationContextUtils.getWebApplicationContext(servletRequest.getServletContext());
        var uw= context.getBean(UserInfoWrapper.class);
        uw.setName("张三");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
