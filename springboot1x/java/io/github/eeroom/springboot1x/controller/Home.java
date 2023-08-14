package io.github.eeroom.springboot1x.controller;

import io.github.eeroom.springboot1x.dao.IStudent;
import io.github.eeroom.springboot1x.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class Home {

    @Resource
    IStudent dalStudent;

    @GetMapping("/home/say")
    public List<Student> Say(){
        var lst= this.dalStudent.getAll();
        return lst;
    }
}
