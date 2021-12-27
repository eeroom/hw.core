package io.github.eeroom.gtop.sf.cors;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AllowAnyOriginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var request=(javax.servlet.http.HttpServletRequest)servletRequest;
        var response=(javax.servlet.http.HttpServletResponse)servletResponse;
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Expose-Headers","Authorization");
        if(!request.getMethod().equalsIgnoreCase("options")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        response.setHeader("Access-Control-Max-Age", "1728000");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void destroy() {

    }
}
