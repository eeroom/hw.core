package org.azeroth.axisclient;

import java.rmi.RemoteException;

public class App {
    public static void main(String[] args) throws Throwable {
        String endpointURL = "http://localhost:49755/Home.asmx";

        org.apache.axis.client.Service service = new org.apache.axis.client.Service();

        org.apache.axis.client.Call call = (org.apache.axis.client.Call) service.createCall();
        call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,false);
        call.setTimeout(60000);
        call.setTargetEndpointAddress(endpointURL);
        call.setSOAPActionURI("http://tempuri.org/HelloWorld");
        call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/","HelloWorld"));// 设置操作的名称。

        call.setReturnType(org.apache.axis.encoding.XMLType.SOAP_STRING);// 返回的数据类型
        call.addParameter(new javax.xml.namespace.QName("http://tempuri.org/","a"),
                org.apache.axis.encoding.XMLType.SOAP_INT,
                javax.xml.rpc.ParameterMode.IN);// 参数的类型

        String result = (String) call.invoke(new Object[]{44});// 执行调用

        System.out.println(result);
    }
}
