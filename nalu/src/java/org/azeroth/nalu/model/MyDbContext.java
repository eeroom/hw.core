package org.azeroth.nalu.model;

import org.azeroth.nalu.DbContext;

import java.sql.Connection;
import java.sql.SQLException;

public class MyDbContext extends DbContext {
    @Override
    protected Connection getConnection() throws SQLException {
        String cnnstr="jdbc:sqlserver://127.0.0.1\\sqlexpress:1433;DatabaseName=hw";
        var cnn= java.sql.DriverManager.getConnection(cnnstr,"sa","123456");
        return cnn;
    }
}
