package io.github.eeroom.jdbc;

import java.sql.SQLException;

public class App {
    public static void main( String[] args ) throws Throwable {
        io.github.eeroom.jdbc.mssql.MssqlTest.query();
        System.out.println( "Hello World!" );
    }
}
