## spring容器
### 基本使用流程
0. 定义bean
    1. 在bean的class上添加注解@Component
    2. 在配置类中定义方法,在方法上添加注解@bean
    3. 命名bean
    4. 指定bean的作用域
    5. 指定bean的
1. 创建容器上下文实例
    ```
    var context= new org.springframework.context.annotation.AnnotationConfigApplicationContext()
    ```
2. 注册对应关系
    1. 直接注册bean名称和实现类的对应关系
        ```
       context.registerBeanDefinition(ChinaSuanfa.class.getName(),new RootBeanDefinition(ChinaSuanfa.class));
       这样也自动建立了实现类和其实现的接口的对应关系
        ```
    2. 利用配置类注册bean名称和实例化该bean的方法
        ```
       context.register(RootConfig.class);
          --------------------------------------
          @Configuration
          public class RootConfig {
              @Bean("mytag")
              public String getlooksee(){
                  return  "look see";
              }
          }
        ```
   3. 利用配置类启用自动扫包注册
        ```
       自动扫描指定package下的具有@Component注解的class,然后把该类注册到context中,bean的名称为该class的全名称
       context.register(RootConfig.class);
       ----------------------------------------
       @ComponentScan
       public class RootConfig {
           
       }
       --------------------------------------
       @Component
       public class Calculation {
           
       }
        ```
3. refesh容器上下文
    ```
   context.refresh();
    ```
4. 获取bean实例
    1. 利用上下文直接获取
        ```
       var cal= context.getBean(ISuanfa.class);
       适用场景：实现类和接口一对一,如果注册多个实现了ISuanfa的class,spring会报错
       
       var lstsuanfa= context.getBeansOfType(ISuanfa.class);
       适用场景:实现类和接口多对一,然后我们需要获取所有实现了该接口的实现类的bean
       ```
    2. 构造函数自动注入,构造函数上添加@Autowired注解。前提条件：该构造函数是由spring调用,也就是该class本身也是注册到spring的ben
    3. 属性和字段注入,在set方法,字段上添加@Autowired注解。前提条件同上
5. 实例的生命周期
    ```
   在bean
   每次启动
   ```
    
       
   
    

