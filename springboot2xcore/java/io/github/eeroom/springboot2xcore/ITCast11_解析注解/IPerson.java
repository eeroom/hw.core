package io.github.eeroom.springboot2xcore.ITCast11_解析注解;

import org.springframework.stereotype.Component;

@TestInheritedForSuperInterface("标注在父接口且具有@Inherited元注解")
@Component("标注在父接口")
public interface IPerson {

}
