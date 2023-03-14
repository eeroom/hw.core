package io.github.eeroom.jdbc.mysql;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Handler {
    public  static void  main2(String[] args ) throws Throwable {
        String cnnstr="jdbc:mysql://localhost:3306/wch?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true";
        try(var cnn= java.sql.DriverManager.getConnection(cnnstr,"root","123456");var st= cnn.createStatement();){
            var cmdstr="select * from Panda";
            try(  var rs= st.executeQuery(cmdstr);){
                while (rs.next()){
                    System.out.println("Id="+rs.getLong("Id"));
                    System.out.println("Name="+rs.getString("Name"));
                }
            }
        }
    }

    public  static void  main(String[] args ) throws Throwable {
        String cnnstr="jdbc:mysql://localhost:3306/wch?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true";
        var csvpath="C:\\Users\\Deroom\\Desktop\\panda.csv";
        int groupId= new Random().nextInt(100000);
        var cdate=new java.sql.Date(new Date().getTime());
        try(var cnn= java.sql.DriverManager.getConnection(cnnstr,"root","123456");
            var reader=new java.io.BufferedReader(new java.io.FileReader(csvpath, Charset.forName("GBK")))){
            cnn.setAutoCommit(false);
            var cmdstr="insert into Panda(Name,GroupId,LocalAddr,Category,CreateTime) values(?,?,?,?,?)";
            var ps= cnn.prepareStatement(cmdstr);
            String datarow;
            var maxSize=1000*50;
            int size=0;
            int totalSize=0;
            var mindate=new Date().getTime();
            while ((datarow= reader.readLine())!=null){
                var dataarray=datarow.split(",");
                ps.setString(1,dataarray[0]);
                ps.setLong(2,groupId);
                ps.setString(3,dataarray[1]);
                ps.setString(4,dataarray[2]);
                ps.setDate(5,cdate);
                ps.executeUpdate();
                size++;
                totalSize++;
                if(size>maxSize){
                    cnn.commit();
                    //ps=cnn.prepareStatement(cmdstr);
                    size=0;
                    System.out.printf("用时%d秒，写入行数:%d\r\n",(new Date().getTime()-mindate)/1000,totalSize);
                }
            }
            if(size>0)
                cnn.commit();
            System.out.printf("结束,用时%d秒，写入行数:%d\r\n",(new Date().getTime()-mindate)/1000,totalSize);
        }

    }
}
