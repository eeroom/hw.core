package org.azeroth.iostream;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class App {


    public static  void  main(String[] args) throws Throwable{
        readTxtFromFile();
    }

    private static void readTxtFromFile() throws Throwable {
        String filePath="D:/tzp.sql";
        String txt;
        try(var reader=java.nio.file.Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            var sb=new StringBuffer();
            var bufferSize=8192;
            var buffer=new char[bufferSize];
            var datalength=0;
            while ((datalength=reader.read(buffer,0,bufferSize))>0){
                sb.append(buffer,0,datalength);
            }
            txt=sb.toString();
        }
        System.out.println(txt);
    }
}
