package org.azeroth.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping(value = "/home/getname",method = RequestMethod.GET)
    @ResponseBody
    public String getName(){
        return "hello world";
    }
}
