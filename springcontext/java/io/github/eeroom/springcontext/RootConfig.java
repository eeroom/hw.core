package io.github.eeroom.springcontext;

import io.github.eeroom.springcontext.bean.MyBeanPostProcessorRegistrar;
import io.github.eeroom.springcontext.context.MyImportSelector;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan
@PropertySource(value = {"classpath:application.properties"})
@EnableAspectJAutoProxy
@Import({MyBeanPostProcessorRegistrar.class, MyImportSelector.class})
public class RootConfig {
    //PropertySource引入配置文件
}
