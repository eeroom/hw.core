package io.github.eeroom.springcore.H06BeanFactory后处理;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Import({MyImportSelector.class})
@Retention(RetentionPolicy.RUNTIME)
public  @interface Dcq{

}
