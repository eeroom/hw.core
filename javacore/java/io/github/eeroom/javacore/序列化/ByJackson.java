package io.github.eeroom.javacore.序列化;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public class ByJackson {

    public static void main(String[] args){
       var lststudent=new ArrayList<Student>();
       var st1=new Student();
       st1.setAge(10);
       st1.setName("张三");
       lststudent.add(st1);
       var json= serializeObject(lststudent);
       System.out.println(json);
       /**
        * 类型擦除，
        * 针对的是定义变量的时候，变量类型会被类型擦除
        * 类型定义的泛型参数不会被擦除，通过类型元数据，可以获取到相关的信息
        * 创建匿名内部类，等价操作就是编译器会创建一个普通类继承基类，这样正好可以规避类型擦除，反序列化的时候可以得到目标类型（泛型类型）
        * 如果普通泛型类，反倒不能实现这个效果！
        */
       var lst2= deSerializeObject(json, new TypeReference<ArrayList<Student>>() {});
       System.out.println(lst2);
    }

    public static String serializeObject(Object jsonstr){
        var objectMapper=new ObjectMapper();
        //反序列的时候，如果json中有额外的字段，class没有与其对应的属性，默认报错，这样配置就不会报错，
        //这个配置对于调用第三方接口特别重要，接口可能增加某个字段数据，但我们不需要用，也就没有修改代码
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            var fdata= objectMapper.writeValueAsString(jsonstr);
            return fdata;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("序列化对象失败：%s",jsonstr.toString()),e);
        }
    }

    public static <T> T deSerializeObject(String content, Class<T> valueType){
        var objectMapper=new ObjectMapper();
        //反序列的时候，如果json中有额外的字段，class没有与其对应的属性，默认报错，这样配置就不会报错，
        //这个配置对于调用第三方接口特别重要，接口可能增加某个字段数据，但我们不需要用，也就没有修改代码
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            var fdata= objectMapper.readValue(content,valueType);
            return fdata;
        } catch (IOException e) {
            throw new RuntimeException(String.format("反序列化数据失败：%s",content),e);
        }
    }

    public static <T> T deSerializeObject(String content, TypeReference<T> trf){
        var objectMapper=new ObjectMapper();
        //反序列的时候，如果json中有额外的字段，class没有与其对应的属性，默认报错，这样配置就不会报错，
        //这个配置对于调用第三方接口特别重要，接口可能增加某个字段数据，但我们不需要用，也就没有修改代码
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            var fdata= objectMapper.<T>readValue(content,trf);
            return fdata;
        } catch (IOException e) {
            throw new RuntimeException(String.format("反序列化数据失败：%s",content),e);
        }
    }

    static  class Student{
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        Integer age;
    }
}
