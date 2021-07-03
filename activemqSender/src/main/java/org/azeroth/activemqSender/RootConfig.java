package org.azeroth.activemqSender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource(value = {ApplicationContext.CLASSPATH_URL_PREFIX+ "application.properties"})
public class RootConfig {
        @Bean
        public org.springframework.jms.core.JmsTemplate createJmsTemplate(org.apache.activemq.ActiveMQConnectionFactory fac ){
            var jtemplate=new org.springframework.jms.core.JmsTemplate(fac);
            //这个以来json类库
            var cc=new org.springframework.jms.support.converter.MappingJackson2MessageConverter();
            jtemplate.setMessageConverter(cc);
            return jtemplate;
        }

        @Value("${activemq.mqurl}")
        String mqurl;

        @Bean
        public org.apache.activemq.ActiveMQConnectionFactory createActiveMQConnectionFactory(){
            var fac=new org.apache.activemq.ActiveMQConnectionFactory(mqurl);
            return fac;
        }
}
