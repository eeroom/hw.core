package io.github.eeroom.springcore.基本使用;

import io.github.eeroom.springcore.基本使用.bean.ISuanfa;
import io.github.eeroom.springcore.基本使用.bean.MyBeanPostProcessorRegistrar;
import io.github.eeroom.springcore.基本使用.context.MyImportSelector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {"io.github.eeroom.springcore.基本使用","io.github.eeroom.springcore.基本使用.bean"})
@PropertySource(value = {"classpath:application.properties"})
@EnableAspectJAutoProxy
@Import({MyBeanPostProcessorRegistrar.class, MyImportSelector.class})
public class RootConfig {
    //PropertySource引入配置文件

    @Value("${mqs.url}")
    String url;

    @Bean
    public String myurl(){
        return this.url+"-----------hello";
    }

    @Bean(name = "taskService4Target")
    public ISuanfa suanfa1(){
        return new ISuanfa() {
            @Override
            public int Hit(int a, int b) {
                return 200;
            }
        };
    }

    @Bean(name = "taskService4Self")
    public ISuanfa suanfa2(){
        return new ISuanfa() {
            @Override
            public int Hit(int a, int b) {
                return 100;
            }
        };
    }
}
