package io.github.eeroom.javacore.assembly;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class 泛型字段的类型元数据 {

    public static void main(String[] args) throws Throwable{
        var mybookMeta= Student.class.getDeclaredField("mybook");
        //泛型类型的类型元数据都是ParameterizedType的子类
        var mybookType=(ParameterizedType)mybookMeta.getGenericType();
        //原始类型，对应于List
        var rawType=mybookType.getRawType();
        System.out.println("rawType:"+rawType);
        //泛型参数的类型，对应于Book
        var parameterType=mybookType.getActualTypeArguments()[0];
        System.out.println("parameterType:"+parameterType);
    }

    static class Student{

        List<Book> mybook;
    }

    static class Book{

    }
}
