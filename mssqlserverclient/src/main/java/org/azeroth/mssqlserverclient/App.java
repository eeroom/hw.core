package org.azeroth.mssqlserverclient;

import java.sql.SQLException;

public class App {
    public static void main( String[] args ) throws Throwable {
        String cnnstr="jdbc:sqlserver://127.0.0.1\\sqlexpress:1433;DatabaseName=hw";
        var cnn= java.sql.DriverManager.getConnection(cnnstr,"sa","123456");
        var st= cnn.createStatement();
        var cmdstr="select * from Log";
        var rs= st.executeQuery(cmdstr);
        while (rs.next()){
            System.out.println("Id="+rs.getLong("Id"));
            System.out.println("Name="+rs.getString("Name"));
        }
        System.out.println( "Hello World!" );
    }
}
