package io.github.eeroom.springboot2xcore.ITCast11_解析注解;

import org.springframework.stereotype.Component;

@Component("直接标注")
@EnableDrive("传参给Card")
public class Person extends Animal implements IPerson{

}
