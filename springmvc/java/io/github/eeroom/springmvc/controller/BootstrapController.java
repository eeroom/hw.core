package io.github.eeroom.springmvc.controller;

import io.github.eeroom.springmvc.aspnet.HttpGet;
import io.github.eeroom.springmvc.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class BootstrapController {

    @HttpGet
    public void biaodan(Model model){
        Student st=new Student();
        st.setAge(11);
        st.setName("张三");
        model.addAttribute("mt",st);
    }
}
