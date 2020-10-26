package org.azeroth.springmvc.controller;

import org.azeroth.springmvc.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

//    实现restapi，需要引入jackson序列化json请求参数和json响应结果
    @RequestMapping(value = "/home/student")
    public Student student(){

        Student st=new Student();
        st.setAge(11);
        st.setName("张三");
        return st;
    }

    public Student add(){
        Student st=new Student();
        st.setAge(22);
        st.setName("add");
        return st;
    }
}
