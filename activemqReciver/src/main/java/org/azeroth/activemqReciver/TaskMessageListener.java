package org.azeroth.activemqReciver;

import javax.jms.Message;
import javax.jms.MessageListener;

public class TaskMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        //这个方法不是被新起线程调用，必须等到这里代码全部执行完，消息监听器才会去获取下一个消息
        System.out.println("收到消息");
    }
}
