package org.azeroth.activemqSender;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Destination;
import javax.jms.Session;

public class App {
    public static void main(String[] args) throws Throwable{
        System.out.println("hello world");
        var fac=new org.apache.activemq.ActiveMQConnectionFactory();
        fac.setBrokerURL("tcp://localhost:61616");
        var cnn= fac.createConnection();
        var session= cnn.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        var msg= session.createTextMessage();
        msg.setText("hell world");

        var target= session.createQueue("udl");
        var producer= session.createProducer(target);
        producer.send(msg);
        producer.close();
        session.close();
        cnn.close();

    }
}
