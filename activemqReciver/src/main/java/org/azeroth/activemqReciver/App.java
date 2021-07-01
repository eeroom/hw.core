package org.azeroth.activemqReciver;

import javax.jms.Session;

public class App {
    public static void main(String[] arg) throws Throwable{
        System.out.println("hello world");
        var fac=new org.apache.activemq.ActiveMQConnectionFactory();
        fac.setBrokerURL("tcp://localhost:61616");
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
