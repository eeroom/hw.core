package io.github.eeroom.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SqliteHandler {


    public static void  query() throws Throwable {

        String cnnstr="jdbc:sqlite:D:/Code/db/zl001-sqlite.db";
        var cnn= java.sql.DriverManager.getConnection(cnnstr);
        var cmdstr="select * from Student where Name = ?";
        var ps= cnn.prepareStatement(cmdstr);
        ps.setString(1,"你好");
        var rs= ps.executeQuery();
        while (rs.next()){
            System.out.println("Id="+rs.getLong("Id"));
            System.out.println("Name="+rs.getString("Name"));
        }
    }

    public static void  queryByIN() throws Throwable {
        String cnnstr="jdbc:sqlite:D:/Code/db/zl001-sqlite.db";
        var cnn= java.sql.DriverManager.getConnection(cnnstr);
        var cmdstr=String.format("select * from Student where Name in(?,?)");
        var ps=cnn.prepareStatement(cmdstr);
        ps.setString(1,"你好");
        ps.setString(2,"aaaa");
        var rs= ps.executeQuery();
        while (rs.next()){
            System.out.println("Id="+rs.getLong("Id"));
            System.out.println("Name="+rs.getString("Name"));
        }
    }

    public static int  add(Student student) throws Throwable {
        String cnnstr="jdbc:sqlite:D:/Code/db/zl001-sqlite.db";
        var cnn= java.sql.DriverManager.getConnection(cnnstr);
        var st= cnn.createStatement();
        var cmdstr="insert into Student(Name,Age) values(?,?)";
        var ps= cnn.prepareStatement(cmdstr);
        ps.setString(1,student.getName());
        ps.setInt(2,student.getAge());
        var rt= ps.executeUpdate();
        return rt;
    }
}
