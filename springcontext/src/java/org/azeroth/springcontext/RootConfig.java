package org.azeroth.springcontext;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan
@PropertySource(value = {"classpath:application.properties"})
@EnableAspectJAutoProxy
public class RootConfig {
    //PropertySource引入配置文件
}
