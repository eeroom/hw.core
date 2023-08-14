package io.github.eeroom.jdbc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class App {
    public static void main( String[] args ) throws Throwable {
        var st=new Student();
        st.setAge(11);
        st.setName("你好");
        st.setChirld(new Student());
        st.getChirld().setAge(89);
        st.getChirld().setName("23sssssssssss");
        var jsonstr= JsonHandler.serialize(st);
        System.out.println( "jsonstr:"+jsonstr );
        var dict=JsonHandler.deserializeToDict(jsonstr);

//        SqlserverHandler.query();
//        SqliteHandler.query();
//        SqliteHandler.queryByIN();
//        SqliteHandler.add(st);
        var lstStudent= MybatisHandler.query();
//        var lstStudent2= MybatisHandler.query2();
        System.out.println(  JsonHandler.serialize(lstStudent));
//        System.out.println(  JsonHandler.serialize(lstStudent2));
    }
}
