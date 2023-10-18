package io.github.eeroom.springcore.基本使用;

import io.github.eeroom.springcore.基本使用.bean.MyBeanPostProcessorRegistrar;
import io.github.eeroom.springcore.基本使用.context.MyImportSelector;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {"io.github.eeroom.springcore.基本使用","io.github.eeroom.springcore.基本使用.bean"})
@PropertySource(value = {"classpath:application.properties"})
@EnableAspectJAutoProxy
@Import({MyBeanPostProcessorRegistrar.class, MyImportSelector.class})
public class RootConfig {
    //PropertySource引入配置文件
}
