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
### DelegatingFilterProxy
```
背景：filter属于servlet范畴,我们往servlet容器添加filter，需要在servlet容器的onStartup阶段中,调用servletcontext的addfilter方法
但是，onStartup阶段,springcontext还没有进行refresh,我们的理想操作就行不通(从springcontext获取filter的bean然后添加到servlet容器)
从而导致filter实例不方便读取配置文件或者获取springcontext中的其他数据
DelegatingFilterProxy可以间接帮我们实现上述的理想操作！
原理：DelegatingFilterProxy实现了servlet规范的filter,
    当我们添加指定的filter的时候，我们并不在onStartup阶段直接添加这个filter
    新的操作为：把这个指定filter注册为spring的bean,并且指定一个beanName
    然后创建DelegatingFilterProxy的实例注册到servlter容器,DelegatingFilterProxy实例有个关键信息就是那个指定filter的beanName
    程序跑起来后,执行到DelegatingFilterProxy实例的时候,DelegatingFilterProxy的实例会从springcontext获取那个beanName的filter bean,然后执行这个filter bean
    这样我们就可以把fitler注册成为bean,然后利用DelegatingFilterProxy和beanName添加这个filter
分析：这是一个典型的适配模式，把从spring容器获取bean延迟filter执行的时候,因为那个时候springcontext一定refresh过了！
    DelegatingFilterProxy实例中获取spring容器的办法就是从servletcontext中获取,因为onStartup阶段,rootcontext被添加到了servletcontext,
    添加的代码在：org.springframework.web.context.ContextLoaderListener.class的父类中
```
### springsecurity
```
关键依赖：spring-security-web,spring-security-config,spring-security-core
开启方式1：在配置类中使用@EnableWebSecurity注解,那么springcontext会注册关键的filter的bean(名称为springSecurityFilterChain)
    然后利用org.springframework.web.filter.DelegatingFilterProxy("springSecurityFilterChain")添加这个关键bean到servlet容器
开启方式2：创建一个org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer的实现类,
    AbstractSecurityWebApplicationInitializer继承自springweb的WebApplicationInitializer,其内部也是往springcontext注册关键bean，后续原理和1类似
```


