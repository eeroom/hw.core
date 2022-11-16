package io.github.eeroom.jdbc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class JsonHandler {

    public static <T>  String serialize(T st) throws Throwable {
        var objectMapper=new ObjectMapper();
        //反序列的时候，如果json中有额外的字段，class没有与其对应的属性，默认报错，这样配置就不会报错，
        //这个配置对于调用第三方接口特别重要，接口可能增加某个字段数据，但我们不需要用，也就没有修改代码
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        var jsonstr= objectMapper.writeValueAsString(st);
        return jsonstr;

    }

    public static HashMap<String,Object>  deserializeToDict(String jsonstr)throws Throwable {
        var objectMapper=new ObjectMapper();
        //反序列的时候，如果json中有额外的字段，class没有与其对应的属性，默认报错，这样配置就不会报错，
        //这个配置对于调用第三方接口特别重要，接口可能增加某个字段数据，但我们不需要用，也就没有修改代码
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        var dict= objectMapper.<HashMap<String,Object>>readValue(jsonstr, new TypeReference<HashMap<String,Object>>(){});

        return  dict;
    }
}
