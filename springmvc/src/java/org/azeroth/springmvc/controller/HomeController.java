package org.azeroth.springmvc.controller;

import org.azeroth.springmvc.model.Student;
import org.azeroth.springmvc.model.UserInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

@RestController
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class HomeController {

    @Autowired
    UserInfoWrapper userInfoWrapper;
//    实现restapi，需要引入jackson序列化json请求参数和json响应结果
    @RequestMapping(value = "/home/student")
    public Student student(){
        Student st=new Student();
        st.setAge(11);
        st.setName("张三");
        return st;
    }

    /**
     * 基于约定：按照controller/action的请求路径监听，
     * 优点：避免写一些重复和意义不大的RequestMapping("/home/student"),借鉴asp.net mvc的做法
     * 前提：控制器添加Controller或者RestController特性，方法没有添加RequestMapping特性，方法为public并且为实例方法
     * @return
     */
    public Student add(){
        Student st=new Student();
        st.setAge(22);
        st.setName(this.userInfoWrapper.getName());
        return st;
    }
}
