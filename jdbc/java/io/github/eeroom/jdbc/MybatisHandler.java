package io.github.eeroom.jdbc;


import org.sqlite.SQLiteConfig;

import javax.sql.DataSource;
import java.util.List;

public class MybatisHandler {
    public static List<Student> query() throws Throwable {
        var resource = "mybatis-config.xml";
        var inputStream = org.apache.ibatis.io.Resources.getResourceAsStream(resource);
        var sqlSessionFactory = new org.apache.ibatis.session.SqlSessionFactoryBuilder().build(inputStream);
        try (var session = sqlSessionFactory.openSession()) {
            var lstStudent =session.<Student>selectList("mytns.selectStudent");
            return lstStudent;
        }
    }

//    public static List<Student> query2() throws Throwable {
//        var cfg=new SQLiteConfig();
//
//        DataSource dataSource = new org.sqlite.SQLiteDataSource();
//
//
//        TransactionFactory transactionFactory = new JdbcTransactionFactory();
//        Environment environment = new Environment("development", transactionFactory, dataSource);
//        Configuration configuration = new Configuration(environment);
//        configuration.addMapper(BlogMapper.class);
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
//        try (var session = sqlSessionFactory.openSession()) {
//            var mapper= session.getMapper(StudentMapper.class);
//            var lstStudent= mapper.getAll();
//            return lstStudent;
//        }
//    }
}
