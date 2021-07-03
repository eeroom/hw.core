package org.azeroth.activemqReciver;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jms.core.JmsOperations;

import javax.jms.ObjectMessage;
import javax.jms.Session;

public class App {
    public static void main(String[] arg) throws Throwable{
        //invoke();
        invokerWithSpring();


    }

    /*
    使用spring的jmstemplate接收消息
     */
    private static void invokerWithSpring() throws Throwable {
        var context=new org.springframework.context.annotation.AnnotationConfigApplicationContext(RootConfig.class);

        var jmo= context.getBean(JmsOperations.class);

        var props= PropertiesLoaderUtils.loadAllProperties("application.properties");
        var queuename= props.getProperty("activemq.queuename");

        while (true){
            var msg= (TaskMessage)jmo.receiveAndConvert(queuename);
            System.out.println("收到消息："+msg.Id);
        }
    }

    /*
    接收消息，最简单的新式，不依赖其他类库
     */
    private static void invoke()throws Throwable {
        System.out.println("hello world");
        var fac=new org.apache.activemq.ActiveMQConnectionFactory("tcp://localhost:61616");
        var cnn= fac.createConnection();
        cnn.start();

        var session= cnn.createSession(false, Session.AUTO_ACKNOWLEDGE);

        var target= session.createQueue("udl");

        var reciver= session.createConsumer(target);
        while (true){

            var msg= (org.apache.activemq.command.ActiveMQTextMessage)reciver.receive();
            System.out.println("收到消息:"+msg.getText());
        }
    }
}
