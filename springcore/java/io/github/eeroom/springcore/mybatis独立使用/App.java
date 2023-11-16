package io.github.eeroom.springcore.mybatis独立使用;

import org.apache.ibatis.binding.MapperProxyFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.core.io.DefaultResourceLoader;

import java.text.MessageFormat;
import java.util.Arrays;

public class App {

    public static void main(String[] args) throws Throwable {
//        var inputStream = org.apache.ibatis.io.Resources.getResourceAsStream("mybatis独立使用4xmlconfig/config.xml");
//        var inputStream2= new DefaultResourceLoader().getResource("classpath:mybatis独立使用4xmlconfig/config.xml").getInputStream();
//        var sqlSessionFactory = new org.apache.ibatis.session.SqlSessionFactoryBuilder().build(inputStream2);
//        try (var session = sqlSessionFactory.openSession()) {
//            var lstStudent =session.<Student>selectList("io.github.eeroom.springcore.mybatis独立使用4xmlconfig.Student.getAll");
//            System.out.println(Arrays.toString(lstStudent.toArray(Student[]::new)));
//        }
        //byjavaconfig();
        byXml();


    }

    static void byXml() throws Throwable {
        var configStream = org.apache.ibatis.io.Resources.getResourceAsStream("mybatis独立使用/config.xml");
        var sessionFactoryBuilder=new org.apache.ibatis.session.SqlSessionFactoryBuilder();
        var sqlSessionFactory=sessionFactoryBuilder.build(configStream);
        try (var session = sqlSessionFactory.openSession()) {
            //方式1，直接使用session的增删改查方法,第一个参数类似于sql语句的id,对于 xml中的：namespace+id
            //说明：xml中的namespace可以是一个接口的全名称，也可以是任意的其它值，如果是其它值，则只能使用如下的方法执行sql
            var lstStudent1 = session.<Student>selectList("io.github.eeroom.springcore.mybatis独立使用.IDaoStudent.getAll");
            lstStudent1.forEach(x->System.out.println(MessageFormat.format("lstStudent1,name:{0}",x.getName())));

            //方式2,获取和namespac对应接口的代理实现，实际就是MapperProxy
            //前提：xml的namespace的值就是对应接口的全名称，id的值就是对应接口中的方法
            var iDaoStudent= session.getMapper(IDaoStudent.class);
            var lstStudent2= iDaoStudent.getAll();
            lstStudent2.forEach(x->System.out.println(MessageFormat.format("lstStudent2,name:{0}",x.getName())));
        }
    }


    private static void byjavaconfig() throws Throwable{
        var cfg=new org.apache.ibatis.session.Configuration();
        var datasource=new org.apache.ibatis.datasource.pooled.PooledDataSource(org.sqlite.JDBC.class.getName(),"jdbc:sqlite:D:/Code/db/springcore-hzkd.db","","");
        var transactionFactory=new org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory();
        var env=new org.apache.ibatis.mapping.Environment("byjavaconfig",transactionFactory,datasource);
        cfg.setEnvironment(env);
        var mapperStream= new DefaultResourceLoader().getResource("classpath:mybatis独立使用4xmlconfig/mapper/student.xml").getInputStream();
        var xmlMapperBuilder = new org.apache.ibatis.builder.xml.XMLMapperBuilder(mapperStream, cfg, "classpath:mybatis独立使用4xmlconfig/mapper/student.xml", cfg.getSqlFragments());
        xmlMapperBuilder.parse();

        var sqlSessionFactory = new org.apache.ibatis.session.SqlSessionFactoryBuilder().build(cfg);
        try (var session = sqlSessionFactory.openSession()) {
            var lstStudent =session.<Student>selectList("io.github.eeroom.springcore.mybatis独立使用4xmlconfig.IDaoStudent.getAll");
            System.out.println(Arrays.toString(lstStudent.toArray(Student[]::new)));

            var iDaoStudent= new MapperProxyFactory<IDaoStudent>(IDaoStudent.class).newInstance(session);
            var lstStudent2= iDaoStudent.getAll();

            var idal= session.getMapper(IDaoStudent.class);
            var lst3= idal.getAll();
            int aaa=4;

        }

        var mapperFactoryBean= new MapperFactoryBean<IDaoStudent>(IDaoStudent.class);
        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
        var ddd= mapperFactoryBean.getObject();
        var lll4= ddd.getAll();
        int bbb=4;

    }


}
