package io.github.eeroom.springboot2xcore.ITCast11_解析注解;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("标注在父类")
@TestInherited("标注在父类且具有@Inherited元注解")
@Scope(value = "标注在父类")
public class Animal{

}
