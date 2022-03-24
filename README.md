# java基础知识demo

## camunda
```
UEL表达式使用场景：设置参数变量或者计算分支条件
内置的变量：task,execution,authenticatedUserId
task实现了DelegateTask接口
execution实现了DelegateExecution接口

设置监听器的3种方法
1、使用Java Class指定，被指定的calss需要实现ExecutionListener或者TaskListener
2、使用Delegate Expression指定，这个就是指定1中class对应的实例对象，需要事先实例对象put到流程变量中
3、使用Expression，e.g.${sfactions.syncAssigneeToBizdatasub(task)};sfactions是一个实现了Serializable接口的类型的实例，需要事先put到流程变量
    然后syncAssigneeToBizdatasub是一个实例方法，参数类型为DelegateTask或者DelegateExecution，然后参数为task或者execution
总结：使用第3种方式，我们可以在创建流程实例的时候注入一个项目通用的listenner处理方法集合的实例进去，等价于一个内置全局变量
```
## IDEA使用技巧
```
打开已存在的项目目录后，idea会默认该目录为一个根模块
可以移除这个默认模块,手动导入对应模块的pom.xml文件,导入之前一定要先选好jdk!!!
如果导入后再选择jdk,可能出现源代码文件各种报错提示,但是编译是正常
大体原因：idea导入pom文件的时候，自己会后台编译然后用于智能提示，但是导之前没选jdk导致其后台编译失败，缺少智能提示需要的程序集，
也就能解释，导入后选择jdk，代码文件（有依赖其它包的时候）提示报错，然后修改被依赖的代码文件，编译，又可以正常提示。因为改了源代码文件，会重新编译

```
