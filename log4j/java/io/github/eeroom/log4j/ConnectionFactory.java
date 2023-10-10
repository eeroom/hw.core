package io.github.eeroom.log4j;

import java.sql.Connection;

public class ConnectionFactory {
    public static Connection getConnection() throws Throwable {
        String cnnstr="jdbc:sqlserver://127.0.0.1\\DEV;DatabaseName=htt;loginTimeout=3;socketTimeout=3000";
        var cnn= java.sql.DriverManager.getConnection(cnnstr,"sa","123456");
        return cnn;
    }
}
