package io.github.eeroom.springmvc.authen;

import io.github.eeroom.springmvc.model.UserInfoWrapper;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import java.io.IOException;

/**
 * 认证的filter
 */
public class AuthenticationFilter implements Filter {
    FilterConfig filterConfig;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig=filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //取到ioc容器
        var context= WebApplicationContextUtils.getWebApplicationContext(servletRequest.getServletContext());
        var uw= context.getBean(UserInfoWrapper.class);
        uw.setName("userName");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
