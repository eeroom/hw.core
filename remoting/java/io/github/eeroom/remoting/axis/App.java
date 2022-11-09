package io.github.eeroom.remoting.axis;

import org.apache.axis.AxisEngine;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.soap.SOAPConstants;

import javax.xml.namespace.QName;

public class App {
    public static void main(String[] args) throws Throwable {
        invokeHelloWorld(44);
        //invokeGetStudent(11);
        //invokeArrayOfAnyType();

    }

    private static void invokeArrayOfAnyType() throws Throwable{
        String endpointURL = "http://localhost:49755/Home.asmx";

        org.apache.axis.client.Service service = new org.apache.axis.client.Service();

        org.apache.axis.client.Call call = (org.apache.axis.client.Call) service.createCall();
        call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,false);
        //call.setTimeout(60000);
        call.setTargetEndpointAddress(endpointURL);
        call.setUseSOAPAction(true);
        call.setSOAPActionURI("http://tempuri.org/Info");
        call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/","Info"));// 设置操作的名称。

        call.setReturnType(XMLType.SOAP_ARRAY);// 返回的数据类型

        Object[] result = (Object[]) call.invoke(new Object[]{});// 执行调用

        System.out.println(result[0]);
    }

    private static void invokeGetStudent(int i) throws Throwable {
        String endpointURL = "http://localhost:49755/Home.asmx";
        String xmlns="http://tempuri.org/";
        String methodName="GetStudentById";
        org.apache.axis.client.Service service = new org.apache.axis.client.Service();

        org.apache.axis.client.Call call = (org.apache.axis.client.Call) service.createCall();
        call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,false);
        //避免生产请求xml中，每个请求参数标签都包含 类型 的 attr
        call.setProperty(AxisEngine.PROP_SEND_XSI,Boolean.FALSE);
        call.setTargetEndpointAddress(endpointURL);
        call.setUseSOAPAction(true);
        call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        call.setSOAPActionURI(xmlns+methodName);
        call.setOperationName(new javax.xml.namespace.QName(xmlns,methodName));// 设置操作的名称。

        //设置复杂请求头
        call.addParameterAsHeader(new QName(xmlns,"Student"),new QName(xmlns,"Student"),
                Student.class,javax.xml.rpc.ParameterMode.IN,javax.xml.rpc.ParameterMode.IN);

        call.registerTypeMapping(Student.class,
                new QName(xmlns,"Student"),
                new org.apache.axis.encoding.ser.BeanSerializerFactory(Student.class,new QName(xmlns,"Student")),
                new org.apache.axis.encoding.ser.BeanDeserializerFactory(Student.class,new QName(xmlns,"Student")));

        call.addParameter(new javax.xml.namespace.QName(xmlns,"id"),
                org.apache.axis.encoding.XMLType.SOAP_INT,
                javax.xml.rpc.ParameterMode.IN);// 参数的类型
        call.setReturnClass(Student.class);
        Student result = (Student) call.invoke(new Object[]{i});// 执行调用

        System.out.println(result.Name);

    }

    private static void invokeHelloWorld(int a) throws Throwable {
        String endpointURL = "http://localhost:49755/Home.asmx";

        org.apache.axis.client.Service service = new org.apache.axis.client.Service();

        org.apache.axis.client.Call call = (org.apache.axis.client.Call) service.createCall();
        call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,false);
        //call.setTimeout(60000);
        call.setTargetEndpointAddress(endpointURL);
        call.setUseSOAPAction(true);
        call.setSOAPActionURI("http://tempuri.org/HelloWorld");
        call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/","HelloWorld"));// 设置操作的名称。

        call.setReturnType(org.apache.axis.encoding.XMLType.SOAP_STRING);// 返回的数据类型
        call.addParameter(new javax.xml.namespace.QName("http://tempuri.org/","a"),
                org.apache.axis.encoding.XMLType.SOAP_INT,
                javax.xml.rpc.ParameterMode.IN);// 参数的类型

        String result = (String) call.invoke(new Object[]{a});// 执行调用

        System.out.println(result);
    }
}
