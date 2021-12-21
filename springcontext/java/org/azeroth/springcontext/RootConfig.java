package org.azeroth.springcontext;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan
@PropertySource(value = {"classpath:application.properties"})
@EnableAspectJAutoProxy
@Import({MyBeanPostProcessorRegistrar.class,MyImportSelector.class})
public class RootConfig {
    //PropertySource引入配置文件
}