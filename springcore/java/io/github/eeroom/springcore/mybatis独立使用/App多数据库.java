package io.github.eeroom.springcore.mybatis独立使用;

import java.text.MessageFormat;

public class App多数据库 {

    public static void main(String[] args) throws Throwable{
        byXml();
        byJavaConfig();
    }

    private static void byJavaConfig() {
        /**
         * 参照原来的javaconfig，多个数据就重复多次即可，两个地方有差异：
         * 1、datasource
         * 2、sql语句的xml文件路径
         * 相比 通过mybatis配置文件的 environments节点配置多个环境，这样的优势：
         * 1、不同的数据库的sqlsession只会有自己数据库的sql语句
         * 2、如果 A 数据库seesion 去获取 B对应的mapper, 会直接报错
         */
    }

    private static void byXml() throws Throwable {
        var configFilePath="mybatis独立使用/config.xml";
        var configStream = org.apache.ibatis.io.Resources.getResourceAsStream(configFilePath);
        var sessionFactoryBuilder=new org.apache.ibatis.session.SqlSessionFactoryBuilder();
        /**
         *  配置文件中:environments default="hz-kd" ,默认使用hz-kd的数据库配置
         *  所以hz-oa数据库的需要显式指定
         *  一个environment对应一个sqlsession

         *  弊端：虽然可以基于多个environment的配置build出多个对应的sqlsessionfactory,但是共用相同的mappers配置
         *      也就是都去扫描解析相同的那些sql语句
         *      但是sql语句又是区分数据库的
         *      如果 A数据库的session去获取 B 对应的mapper ,可以正常获取到，但是执行方法 的时候 会报错，表不存在！
         *          如果刚好A也有一个和B同名同结构的表，执行方法也能成功，但是和业务预期不一致！
         *  待研究：
         *    xml中的sql语句有可选属性：databaseId="hz-oa"
         */
        var sqlSessionFactory=sessionFactoryBuilder.build(configStream,"hz-oa");
        try (var session = sqlSessionFactory.openSession()) {
            //hz-oa数据库的session获取自己对应得mapper
            var iDaoTeacher= session.getMapper(IDaoTeacher.class);
            var lstTeacher= iDaoTeacher.getAll();
            lstTeacher.forEach(x->System.out.println(MessageFormat.format("lstStudent2,name:{0}",x.getName())));

            //hz-oa数据库的session获取hz-kd对应得mapper，执行阶段才会报错：表不存在
            var iDaoStudent= session.getMapper(IDaoStudent.class);
            var lstStudent2= iDaoStudent.getAll();
            lstStudent2.forEach(x->System.out.println(MessageFormat.format("lstStudent2,name:{0}",x.getName())));
        }

    }
}
