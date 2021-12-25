package io.github.eeroom.hz.controller;

import io.github.eeroom.hz.aspnet.HttpGet;
import io.github.eeroom.hz.authen.SkipAuthentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class Test {

    @HttpGet
    @SkipAuthentication
    public List<Integer> list(){
        var lst= Stream.of(1,2,4).collect(Collectors.toList());
        return lst;
    }

    @HttpGet
    public List<Integer> listByLoginIn(){
        var lst= Stream.of(1,2,4).collect(Collectors.toList());
        return lst;
    }
}
