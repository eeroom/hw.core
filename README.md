# java基础知识demo
package -P production
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
## maven插件
1. maven-jar-plugin 关键点就是设定MAINFEST.MF文件里的一些值,比如运行的Main class、classpath路径等等,把普通jar做成可以直接运行的jar,最简配置如下
    ```
    <plugin>
        <artifactId>maven-jar-plugin</artifactId>
        <version>3.0.2</version>
        <configuration>
            <classesDirectory>target/classes/</classesDirectory>
            <archive>
                <manifest>
                    <mainClass>这里要替换成jar包main方法所在类</mainClass>
                    <!-- 打包时 MANIFEST.MF文件不记录的时间戳版本 -->
                    <useUniqueVersions>false</useUniqueVersions>
                    <addClasspath>true</addClasspath>
                    <classpathPrefix>lib/</classpathPrefix>
                </manifest>
            </archive>
        </configuration>
    </plugin>
    ```
2. maven-dependency-plugin 编译完成后,把所有依赖的类库汇总到指定目录,类似于c#的默认编译配置
    ```
   前提是使用maven-compiler-plugin插件进行编译
   <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <!-- maven-dependency-plugin插件在maven的根pom.xml配置文件中有，位于节点pluginManagement-->
       <artifactId>maven-dependency-plugin</artifactId>
       <executions>
           <execution>
               <id>copy-dependencies</id>
               <phase>package</phase>
               <goals>
                   <goal>copy-dependencies</goal>
               </goals>
               <configuration>
                   <type>jar</type>
                   <includeTypes>jar</includeTypes>
                   <outputDirectory>
                       ${project.build.directory}/lib
                   </outputDirectory>
               </configuration>
           </execution>
       </executions>
   </plugin>
   ```
3. maven-assembly-plugin 作用和maven-dependency-plugin一致,区别:依赖也被打包到目标jar里面,类似c#嵌入式资源文件的做法
    ```
   <plugin>
       <!-- maven3.6本身的根配置文件pom.xml在pluginManagement节点已经配置了这个插件，版本较低且为beta版本-->
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-assembly-plugin</artifactId>
       <version>3.1.1</version>
       <configuration>
           <archive>
               <manifest>
                   <!--这里要替换成jar包main方法所在类 -->
                   <mainClass>这里要替换成jar包main方法所在类</mainClass>
               </manifest>
               <manifestEntries>
                   <Class-Path>.</Class-Path>
               </manifestEntries>
           </archive>
           <descriptorRefs>
               <descriptorRef>jar-with-dependencies</descriptorRef>
           </descriptorRefs>
       </configuration>
       <executions>
           <execution>
               <id>make-assembly</id> <!-- this is used for inheritance merges -->
               <phase>package</phase> <!-- 指定在打包节点执行jar包合并操作 -->
               <goals>
                   <goal>single</goal>
               </goals>
           </execution>
       </executions>
   </plugin>
   ```
4. maven打包实现配置文件外置
```
等价于App.config或者Web.config方便程序部署后修改配置
关键是配置build>resources>resource节点，对resource指定targetpath属性，就可以把resource中指定的文件在打包过程中放到指定目录
spring boot程序会默认读取 congfig/application.yml>classpath:application.yml 的配置文件信息，所以把targetpath属性指定为${project.build.directory}/config即可
最佳实践：在ideo里普通调试或运行场景必须配置文件打进jar文件里，否则程序跑不起来，原因未深究；利用profile，配置targetpath，然后只有正式发布等特定场景打的包实现外置配置文件
```
## tomcat服务器
```
发布程序到服务器
把war文件复制到tomcat的webapps目录，比如springmvc.war,tomcat默认会自动解压缩到同名目录
打开浏览器访问地址,http://tomcat根/springmvc即可,等价于打开http://tomcat根/springmvc/index.html

可选配置:热更新,关键是reloadable="true",已知问题：如果某个依赖的jar文件被占用卡死,会导致热更新的时候无法删除原目录，从而更新失败
修改conf\server.xml,<Host></Host>节下增加一条，<Context path="/springmvc" docBase="springmvc" reloadable="true"/>

发布程序到根目录的方法：tomcat默认以webapps/ROOT文件夹为根目录，所以只需要把程序放到ROOT目录即可,或者把war文件命名为ROOT.war

支持中文url路径的配置：URIEncoding="UTF-8"
<Connector port="8080" protocol="HTTP/1.1"
   connectionTimeout="20000"
   URIEncoding="UTF-8"
   redirectPort="8443" />

tomcat虚拟内存JVM设置，
直接运行版，修改catalina.bat中增加
-Xms 初始堆大小 如：-Xms256m或2G
-Xmx 最大堆大小
-XX:PermSize 永久代(方法区)的初始大小
-XX:MaxPermSize 永久代(方法区)的最大值
set JAVA_OPTS=-Xms512m -Xmx512m -XX:PermSize=256m -XX:MaxPermSize=256m
window服务版，打开tomcat7w.exe(图形界面)，打开Java选项卡，在Java Options中追加
-XX:PermSize=256m-XX:MaxPermSize=256m
然后修改表单项
Initial memory pool和Maximum memory pool的值
```
## 集合体系
## IO体系
## 日期时间及平台信息
## 多线程和同步
## 网络
