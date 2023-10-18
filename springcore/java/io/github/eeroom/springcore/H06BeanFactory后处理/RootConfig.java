package io.github.eeroom.springcore.H06BeanFactory后处理;

import io.github.eeroom.springcore.基本使用.bean.MyBeanPostProcessorRegistrar;
import io.github.eeroom.springcore.基本使用.context.MyImportSelector;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {"io.github.eeroom.springcore.H06BeanFactory后处理"})
@PropertySource(value = {"classpath:application.properties"})
public class RootConfig {
    //PropertySource引入配置文件
}
