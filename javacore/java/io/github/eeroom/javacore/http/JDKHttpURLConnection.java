package io.github.eeroom.javacore.http;

import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class JDKHttpURLConnection {
    public static void main(String[] args) throws Throwable{
        doPost();

    }

    private static void doPost() throws Throwable{
        String url="http://localhost:49755/Home.asmx";
        HashMap<String,String> header=new HashMap<>();
        header.put("Content-Type","text/xml; charset=utf-8");
        header.put("SOAPAction","\"http://tempuri.org/Seek\"");

        String parameterValue="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <TokenWraper xmlns=\"http://tempuri.org/\">\n" +
                "      <Jwt>abc123456</Jwt>\n" +
                "    </TokenWraper>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <Seek xmlns=\"http://tempuri.org/\">\n" +
                "      <st>\n" +
                "        <Age>101</Age>\n" +
                "        <Name>张三</Name>\n" +
                "      </st>\n" +
                "      <age>102</age>\n" +
                "    </Seek>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        var httpcnn=(HttpURLConnection)new URL(url).openConnection();
        httpcnn.setRequestMethod("POST");
        httpcnn.setDoOutput(true);
        for (var kv:header.entrySet()){
            httpcnn.setRequestProperty(kv.getKey(),kv.getValue());
        }
        var payload=parameterValue.getBytes(StandardCharsets.UTF_8);
        httpcnn.getOutputStream().write(payload,0,payload.length);
        var sb=new StringBuilder();
        int bufferSize=8192,datalength=0;
        var buffer=new char[bufferSize];
        try (var reader=new InputStreamReader(httpcnn.getInputStream(),StandardCharsets.UTF_8)){
            while ((datalength=reader.read(buffer,0,bufferSize))>0){
                sb.append(buffer,0,datalength);
            }
            var value=sb.toString();
            System.out.println("响应内容:"+value);
        }catch (Throwable throwable){
            throw new RuntimeException("调用url发送异常:"+url,throwable);
        }
    }
}
