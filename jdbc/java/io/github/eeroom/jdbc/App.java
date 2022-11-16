package io.github.eeroom.jdbc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.HashMap;

public class App {
    public static void main( String[] args ) throws Throwable {
        var objectMapper=new ObjectMapper();
        //反序列的时候，如果json中有额外的字段，class没有与其对应的属性，默认报错，这样配置就不会报错，
        //这个配置对于调用第三方接口特别重要，接口可能增加某个字段数据，但我们不需要用，也就没有修改代码
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        var st=new Student();
        st.setAge(11);
        st.setName("你好");
        st.setChirld(new Student());
        st.getChirld().setAge(89);
        st.getChirld().setName("23sssssssssss");
        var jsonstr= objectMapper.writeValueAsString(st);
        var dict= objectMapper.readValue(jsonstr, new TypeReference<HashMap<String,Object>>(){});



        //io.github.eeroom.jdbc.mssql.MssqlTest.query();

        SqliteHandler.query();
        System.out.println( "Hello World!" );
    }

    static class Student{
        Integer age;

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String name;

        public Student getChirld() {
            return chirld;
        }

        public void setChirld(Student chirld) {
            this.chirld = chirld;
        }

        Student chirld;
    }
}
