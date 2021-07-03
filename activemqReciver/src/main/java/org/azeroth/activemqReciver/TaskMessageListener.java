package org.azeroth.activemqReciver;

import javax.jms.Message;
import javax.jms.MessageListener;

public class TaskMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println("收到消息");
    }
}
