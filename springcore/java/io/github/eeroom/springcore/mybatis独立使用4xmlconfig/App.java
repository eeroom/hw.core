package io.github.eeroom.springcore.mybatis独立使用4xmlconfig;

import org.springframework.core.io.DefaultResourceLoader;

import java.util.Arrays;

public class App {

    public static void main(String[] args) throws Throwable {
        var inputStream = org.apache.ibatis.io.Resources.getResourceAsStream("mybatis独立使用4xmlconfig/config.xml");
        var inputStream2= new DefaultResourceLoader().getResource("classpath:mybatis独立使用4xmlconfig/config.xml").getInputStream();
        var sqlSessionFactory = new org.apache.ibatis.session.SqlSessionFactoryBuilder().build(inputStream2);
        try (var session = sqlSessionFactory.openSession()) {
            var lstStudent =session.<Student>selectList("io.github.eeroom.springcore.mybatis独立使用4xmlconfig.Student.getAll");
            System.out.println(Arrays.toString(lstStudent.toArray(Student[]::new)));
        }
    }
}
