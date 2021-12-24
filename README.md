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
