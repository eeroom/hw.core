package io.github.eeroom.remoting.axis;

import org.apache.axis.AxisEngine;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.axis.soap.SOAPConstants;

import javax.xml.namespace.QName;


public class App {
    public static void main(String[] args) throws Throwable {
        //invokeHelloWorld(44);
        //invokeGetStudent(11);
        //invokeArrayOfAnyType();
        invokeWithHeader();

    }

    private static void invokeWithHeader() throws Throwable {
        String endpointURL = "http://localhost:49755/Home.asmx";
        String xmlns="http://tempuri.org/";
        var xmlns1="http://www.w3.org/2001/XMLSchema";
        String methodName="Seek";
        org.apache.axis.client.Service service = new org.apache.axis.client.Service();

        org.apache.axis.client.Call call = (org.apache.axis.client.Call) service.createCall();
        //非常重要，否则.net的webservice接收不到自定义实体的参数，简单类型参数不受影响
        var style= call.getEncodingStyle();
        call.setEncodingStyle(null);

        call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,Boolean.FALSE);
        //避免生产请求xml中，每个请求参数标签都包含 类型 的 attr,Disable sending xsi:type
        call.setProperty(AxisEngine.PROP_SEND_XSI,Boolean.FALSE);
        // XML with new line
        call.setOption(org.apache.axis.AxisEngine.PROP_DISABLE_PRETTY_XML, Boolean.FALSE);
        call.setProperty(AxisEngine.PROP_ENABLE_NAMESPACE_PREFIX_OPTIMIZATION,Boolean.FALSE);
        call.setProperty(AxisEngine.PROP_DOTNET_SOAPENC_FIX,Boolean.TRUE);

        call.setTargetEndpointAddress(endpointURL);
        call.setUseSOAPAction(true);
        call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        call.setSOAPActionURI(xmlns+methodName);
        call.setOperationName(new javax.xml.namespace.QName(xmlns,methodName));// 设置操作的名称。
        call.setOperationStyle(Style.WRAPPED);//和服务端的webservice方法对应，.net的默认就是WRAPPED，axis默认的也是WRAPPED，可以通过添加特性修改
        call.setOperationUse(Use.DEFAULT);//和服务端的webservice方法对应，可以通过添加特性修改

        var header=new SOAPHeaderElement(xmlns,"TokenWraper");
        header.setNamespaceURI(xmlns);
        header.addChildElement("Jwt").setValue("aaaaaaaaaaaaaavvvvvvvvvvvvv");

        call.addHeader(header);

        call.registerTypeMapping(Student.class,
                new QName(xmlns,"Student"),
                new org.apache.axis.encoding.ser.BeanSerializerFactory(Student.class,new QName(xmlns,"Student")),
                new org.apache.axis.encoding.ser.BeanDeserializerFactory(Student.class,new QName(xmlns,"Student")));

        call.addParameter(new javax.xml.namespace.QName(xmlns,"st"),
                new QName(xmlns,"Student"),
                Student.class,
                javax.xml.rpc.ParameterMode.IN);// 参数的类型
        call.addParameter(new javax.xml.namespace.QName(xmlns,"age"),
                org.apache.axis.encoding.XMLType.SOAP_INT,

                javax.xml.rpc.ParameterMode.IN);// 参数的类型
        call.setReturnType(XMLType.XSD_STRING);
        var st=new Student();
        st.Age=101;
        st.Name="张三aaa";
        var result = (String) call.invoke(new Object[]{st,33});// 执行调用

        System.out.println(result);
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
