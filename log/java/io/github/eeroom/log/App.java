package io.github.eeroom.log;

import io.github.eeroom.log.log4j.Student;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class App {
    public static void main( String[] args )
    {
        /**
         * 默认读取classpath目录下的log4j2.xml
         * 等价于 System.setProperty("log4j.configurationFile","classpath:log4j2.xml");
         * 弊端：配置文件被打进了jar文件，部署后不能修改配置
         *
         * 按照如下设置
         * 1、开发的时候，可以直接运行和调试，因为log4j2.xml文件会被复制到target/classes目录中，idea会把这个目录设置为classpath
         * 2、打生产包，通过pom.xml的build配置，增加-P production参数，从而把log4j2.xml复制到目标根目录，不打进jar文件
         * 3、默认打包，log4j2.xml打进jar文件
         * 原理：log4j2会尝试从classpath目录和user.dir目录读取log4j2.xml文件，所以上述3中情况都可以正常运行
         * 优点：1、开发阶段维持默认处理。2、部署阶段jar和配置文件分离，方便修改配置文件
         */
        System.setProperty("log4j.configurationFile","log4j2.xml");
        Logger log= org.apache.logging.log4j.LogManager.getLogger();
        Student st=new Student();
        st.Age=11;
        st.Name="张三222333";
        while (true){
            log.error(st);
        }
    }
}
