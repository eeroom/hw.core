### springmvc
## servlet容器规范
## springmvc和servlet容器整合的原理
```
基于容器规范添加关键的servlet
利用servlet容器的listeners事件回调refresh rootcontext,
利用servlet的init约定refresh webcontext,在refresh之前，会注册springcontext的eventlistener，
    一旦webcontext refresh完成,eventlistener的回调方法里面进行mvc相关的配置。配置数据都定义为webcontext的bean,

```
### spring的配置类技巧
```
@org.springframework.context.annotation.Configuration是继承自@Component
如果其它地方配置了自动扫包,那么@org.springframework.context.annotation.Configuration的配置类也会被扫
对于一个可选的、验证性的涉及第三方引入相关的配置类,可以把配置类建在自动扫包basedir的往上一级,避免被springcontext扫包
```
### springsecurity
```
关键依赖：spring-security-web,spring-security-config,spring-security-core
开启方式1：在配置类中使用@EnableWebSecurity注解,那么springcontext会注册关键的filter的bean(名称为springSecurityFilterChain)
    然后利用org.springframework.web.filter.DelegatingFilterProxy("springSecurityFilterChain")添加这个关键bean到servlet容器
开启方式2：创建一个org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer的实现类,
    AbstractSecurityWebApplicationInitializer继承自springweb的WebApplicationInitializer,其内部也是往springcontext注册关键bean，后续原理和1类似
```

