package io.github.eeroom.jdbc;

import java.sql.SQLException;

public class SqliteHandler {

    public static void  query() throws Throwable {
        String cnnstr="jdbc:sqlite:D:/Code/db/zl001-sqlite.db";
        var cnn= java.sql.DriverManager.getConnection(cnnstr);
        var st= cnn.createStatement();
        var cmdstr="select * from Student";
        var rs= st.executeQuery(cmdstr);
        while (rs.next()){
            System.out.println("Id="+rs.getLong("Id"));
            System.out.println("Name="+rs.getString("Name"));
        }
    }
}
