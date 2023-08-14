package org.springframework.context.annotation;

@Configuration
public class RootConfig {

    @Bean
    public Bean1 bean1(){
        return new Bean1();
    }

    @Bean
    public Bean2 bean2(Bean1 bean1){
        var bean11=this.bean1();
        System.out.println("bean1:"+bean1);
        System.out.println("bean11:"+bean11);
        return new Bean2();
    }

    static class Bean1{

    }

    static class Bean2{

    }
}
