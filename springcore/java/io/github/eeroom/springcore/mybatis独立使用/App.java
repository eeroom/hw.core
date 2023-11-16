package io.github.eeroom.springcore.mybatis独立使用;

import org.apache.ibatis.binding.MapperProxyFactory;

import java.text.MessageFormat;

public class App {

    public static void main(String[] args) throws Throwable {
        byXml();
        byJavaConfig();
    }

    static void byXml() throws Throwable {
        var configFilePath="mybatis独立使用/config.xml";
        var configStream = org.apache.ibatis.io.Resources.getResourceAsStream(configFilePath);
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

    static void byJavaConfig() throws Throwable{
        //mybatis内部会把xml配置文件解析为configuration,这里就是内部解析xml的简化版，sessionFactoryBuilder.build(configStream)即可看到内部过程
        //一个environment对应一个sqlsession
        var cnnstr="jdbc:sqlite:D:/Code/db/springcore-hzkd.db";
        var datasource=new org.apache.ibatis.datasource.pooled.PooledDataSource(org.sqlite.JDBC.class.getName(),cnnstr,"","");
        var transactionFactory=new org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory();
        var env= new org.apache.ibatis.mapping.Environment.Builder("db-hzkd")
                .dataSource(datasource)
                .transactionFactory(transactionFactory)
                .build();
        var configuration=new org.apache.ibatis.session.Configuration(env);

        var mapperFilePath= "mybatis独立使用/mapper/Student.xml";
        var mapperStream=org.apache.ibatis.io.Resources.getResourceAsStream(mapperFilePath);
        var xmlMapperBuilder = new org.apache.ibatis.builder.xml.XMLMapperBuilder(mapperStream, configuration, mapperFilePath, configuration.getSqlFragments());
        xmlMapperBuilder.parse();

        var sessionFactoryBuilder=new org.apache.ibatis.session.SqlSessionFactoryBuilder();
        var sqlSessionFactory = sessionFactoryBuilder.build(configuration);
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

            //方式3，方式2的内部简化形式
            //xmlMapperBuilder.parse();方法内部的this.bindMapperForNamespace();会增加 接口对应的mapper，mapper实际上就是一个MapperProxy的实例
            var mapperProxyFactory= new MapperProxyFactory<IDaoStudent>(IDaoStudent.class);
            var mapperProxy =mapperProxyFactory.newInstance(session);
            var lstStudent3= mapperProxy.getAll();
            lstStudent3.forEach(x->System.out.println(MessageFormat.format("lstStudent3,name:{0}",x.getName())));
        }
    }


}
