package io.github.eeroom.activemqReciver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import java.util.HashMap;

@Configuration
@ComponentScan
@PropertySource(ApplicationContext.CLASSPATH_URL_PREFIX+"application.properties")
public class RootConfig {

    @Bean
    @Scope("prototype")
    public org.springframework.jms.core.JmsTemplate createJmsTemplate(ConnectionFactory factory){
        var jmstemplate=new org.springframework.jms.core.JmsTemplate(factory);
        var cc=new org.springframework.jms.support.converter.MappingJackson2MessageConverter();
        cc.setTargetType(MessageType.TEXT);
        // 定义了typeId到Class的Map
        var typeIdMap = new HashMap<String, Class<?>>();
        typeIdMap.put("TaskMessage", TaskMessage.class);
        cc.setTypeIdMappings(typeIdMap);
        // 设置发送到队列中的typeId的名称
        cc.setTypeIdPropertyName("TaskMessage");
        cc.setEncoding("UTF-8");
        jmstemplate.setMessageConverter(cc);
        return jmstemplate;
    }

    @Value("${activemq.mqurl}")
    String mqurl;

    @Value("${activemq.queuename}")
    String queuename;

    @Bean
    public ConnectionFactory createConnectionFactory(){
        var fac=new org.apache.activemq.ActiveMQConnectionFactory(mqurl);
        return fac;
    }

    @Bean
    public org.springframework.jms.listener.MessageListenerContainer create(ConnectionFactory fac){
        var lcontainer=new org.springframework.jms.listener.DefaultMessageListenerContainer();
        lcontainer.setConnectionFactory(fac);
        var listener=new TaskMessageListener();
        lcontainer.setMessageListener(listener);
        lcontainer.setDestinationName(queuename);
        return lcontainer;
    }


}
