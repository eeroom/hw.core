package io.github.eeroom.cloudkd.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

    @Value("${server.port}")
    int port;

    @GetMapping("home/say")
    public String say(){
        return "kd server port:"+this.port;
    }
}
