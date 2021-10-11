package org.azeroth.workflow;

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
        //读取在contextInitialized配置的filter初始化参数
        var a1= this.filterConfig.getInitParameter("a1");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //取到ioc容器
        System.out.println("AuthenticationFilter处理请求前");
        var context= WebApplicationContextUtils.getWebApplicationContext(servletRequest.getServletContext());
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("AuthenticationFilter处理请求后");
    }

    @Override
    public void destroy() {

    }
}
