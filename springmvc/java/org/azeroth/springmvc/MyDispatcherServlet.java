package org.azeroth.springmvc;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyDispatcherServlet extends DispatcherServlet {
    public MyDispatcherServlet(WebApplicationContext context){
        super(context);
    }
    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //加一些自己的代码，类似于框架的拦截器
        //这里可以把当前请求的用户信息取出来，复制到rquest作用域的用户信息，方便其他的地方从ioc取当前请求的用户信息
        //取当前用户信息也可以在filter里面做，需要对severletcontext添加一个lister，参见app.registerContextLoaderListener方法
        System.out.println("处理请求前");
        super.doDispatch(request, response);
        System.out.println("处理请求后");
    }
}
