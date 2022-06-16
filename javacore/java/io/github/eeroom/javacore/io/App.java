package io.github.eeroom.javacore.io;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

public class App {


    public static  void  main(String[] args) throws Throwable{
        //bio,读取小文件,获取所有字节内容
        String filePath="C:\\Code\\222.txt";
        try (var fs=new FileInputStream(filePath)){
                var buffers= fs.readAllBytes();
                var tmp=3;
        }
        //bio,读取大文件，使用固定字节数组读取
        try (var fs=new FileInputStream(filePath)){
                var bufferSize=8192;
                var buffer=new byte[bufferSize];
                var length=0;
                while ((length=fs.read(buffer,0,bufferSize))>0){
                    for (int i=0;i<length;i++){
                        System.out.print(Integer.toHexString(Byte.toUnsignedInt(buffer[i])));
                        System.out.print("-");
                    }
                }
            System.out.print("\r\n");
        }
        //bio,读取大或小文本文件，获取文本内容
        try (var reader=new FileReader(filePath,Charset.forName("utf-8"))){
            var bufferSize=8192;
            var buffer=new char[bufferSize];
            var length=0;
            var sb=new StringBuilder();
            while ((length=reader.read(buffer,0,bufferSize))>0){
                sb.append(buffer);
            }
            var str=sb.toString();
            System.out.println(str);
        }

        var sc=new Scanner(System.in);
        var abc= sc.next();
        System.out.println(abc);
        //nio
        //aio
        var sstr= new BufferedReader(new InputStreamReader(System.in,"utf-8")).readLine();
        System.out.println(sstr);

    }
}
