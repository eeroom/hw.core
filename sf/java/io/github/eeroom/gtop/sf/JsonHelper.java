package io.github.eeroom.gtop.sf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonHelper {
    com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public JsonHelper(){
        this.objectMapper=new ObjectMapper();
        //反序列的时候，如果json中有额外的字段，class没有与其对应的属性，默认报错，这样配置就不会报错，
        //这个配置对于调用第三方接口特别重要，接口可能增加某个字段数据，但我们不需要用，也就没有修改代码
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public String serializeObject(Object jsonstr){
        try {
            var fdata= this.objectMapper.writeValueAsString(jsonstr);
            return fdata;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T deSerializeObject(String content, Class<T> valueType){
        try {
            var fdata= this.objectMapper.readValue(content,valueType);
            return fdata;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T deSerializeObject(String content,TypeReference<T> trf){
        try {
            var fdata= this.objectMapper.<T>readValue(content,trf);
            return fdata;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
