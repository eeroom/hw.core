package io.github.eeroom.springboot1x.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

    @GetMapping("/home/say")
    public String Say(){
        return "helo owlr";
    }
}
