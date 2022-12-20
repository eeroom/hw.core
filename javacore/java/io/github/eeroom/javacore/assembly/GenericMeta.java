package io.github.eeroom.javacore.assembly;

import java.io.Console;

/**
 * 泛型，类型擦除
 * 某些场景需要获取泛型的类型元数据
 */
public class GenericMeta {
    public static void main(String[] args){
        //定义变量的时候，会被类型擦除，反射无法获取泛型的类型参数信息
       var dw=new DataWrapper<String>();
       var meta= dw.getClass().getGenericSuperclass();
        System.out.println(meta);
       //定义匿名内部类的变量，编译器会自动创建一个普通类继承基类，变量的真实类型就是编译器自动创建的这个类
        // 定义类型的时候，泛型的类型参数不会被擦除，反射可以获取泛型的类型参数
       var dw2=new DataWrapperV2<String>(){};
       var meta2=dw2.getClass().getGenericSuperclass();
        System.out.println("meta2:"+meta2);
       //匿名内部类的等价操作
        var dw3=new DataWrapperV3();
        var meta3= dw3.getClass().getGenericSuperclass();
        System.out.println("meta3:"+meta3);
    }

    static class DataWrapper<T>{
        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        T value;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        int code;
    }

    static abstract class DataWrapperV2<T>{
        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        T value;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        int code;
    }

    static class DataWrapperV3 extends DataWrapperV2<String>{

    }
}
