package io.github.eeroom.activemqSender;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jms.core.JmsOperations;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import javax.jms.Destination;
import javax.jms.Session;

public class App {
    public static void main(String[] args) throws Throwable{
        //invoke();
        invokeWithSpring();

    }

    /*
    发送消息，最简单的形式，不依赖其他类库
     */
    private static void invoke() throws Throwable{
        System.out.println("hello world");
        var fac=new org.apache.activemq.ActiveMQConnectionFactory("tcp://localhost:61616");
        var cnn= fac.createConnection();
        //第一个参数，true为开启事务，false不开启事物
        var session= cnn.createSession(true, Session.AUTO_ACKNOWLEDGE);
        var msg= session.createTextMessage();
        msg.setText("hell world");

        var target= session.createQueue("udl");
        var producer= session.createProducer(target);
        producer.send(msg);
        //事物session,消息send之后需要commit,场景：一个事情需要发送多个消息
        session.commit();

        producer.close();
        session.close();
        cnn.close();
    }

    static void invokeWithSpring() throws Throwable{
        var context=new org.springframework.context.annotation.AnnotationConfigApplicationContext(RootConfig.class);

        var jmo= context.getBean(JmsOperations.class);

        var props= PropertiesLoaderUtils.loadAllProperties("application.properties");
        var queuename= props.getProperty("activemq.queuename");
        var msg=new TaskMessage();
        msg.Age=100;
        msg.Id="se";
        jmo.convertAndSend(queuename,msg);

    }
}
