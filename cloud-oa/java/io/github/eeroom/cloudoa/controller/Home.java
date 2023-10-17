package io.github.eeroom.cloudoa.controller;

import io.github.eeroom.cloudoa.service.cloudkd.IHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

    @Autowired
    IHome kdhome;

    @GetMapping("home/say")
    public String say(){
        return this.kdhome.say();
    }
}
