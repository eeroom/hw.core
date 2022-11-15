package io.github.eeroom.remoting.axis2;

import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axis2.Constants;

public class App {
    public static void main(String[] args) throws Throwable {
        helloworld();

    }

    private static void helloworld() throws Throwable {
        var client=new org.apache.axis2.client.ServiceClient();
        var options= client.getOptions();
        options.setSoapVersionURI(org.apache.axiom.soap.SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        options.setTransportInProtocol(org.apache.axis2.Constants.TRANSPORT_HTTP);
        options.setAction("http://tempuri.org/HelloWorld");
        options.setTo(new org.apache.axis2.addressing.EndpointReference("http://localhost:49755/Home.asmx"));

        var fac=org.apache.axiom.om.OMAbstractFactory.getOMFactory();
        var tns=fac.createOMNamespace("http://tempuri.org/","");
        var a=fac.createOMElement("a",tns);
        a.setText("101");
        var method=fac.createOMElement("HelloWorld",tns);
        method.addChild(a);

        var ome= client.sendReceive(method);
        System.out.print(ome.getFirstElement().toString());
    }

}
