package io.github.eeroom.hzkd;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class MyDispatcherServlet extends DispatcherServlet {
    WebApplicationContext context;
    public MyDispatcherServlet(WebApplicationContext context){
        super(context);
        this.context=context;
    }
    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //加一些自己的代码，类似于框架的拦截器
        //这里可以把当前请求的用户信息取出来，复制到rquest作用域的用户信息，方便其他的地方从ioc取当前请求的用户信息
        //取当前用户信息也可以在filter里面做，需要对severletcontext添加一个lister，参见app.registerContextLoaderListener方法
        var httpContext= (Map<String,Object>)this.context.getBean("httpContext");
        httpContext.put("Method",request.getMethod());
        httpContext.put("ContentType",request.getContentType());
        super.doDispatch(request, response);
    }
}
