package io.github.eeroom.gtop.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.eeroom.apiclient.IUnWrapper;

import java.io.IOException;

public class ApidataWrapper implements IUnWrapper {
    public ApidataWrapper(){
        this.code= 200;
    }
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    /**
     * javax.servlet.http.HttpServletResponse.SC*****;
     * @param code
     */
    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;

    }

    Object data;
    int code;
    String message;
    Object tag;

    @Override
    public Object unwrapper(Class<?> targetmeta) {
        var jsonConvert=new JsonConvert();
        if(this.code!=200)
            throw new RuntimeException(String.format("目标api的响应结果响应码不为200，响应结果：%s",jsonConvert.serializeObject(this)));
        if(targetmeta.equals(void.class))
            return null;
        return jsonConvert.deSerializeObject(jsonConvert.serializeObject(this.data),targetmeta);
    }

    static class JsonConvert {
        com.fasterxml.jackson.databind.ObjectMapper objectMapper;

        public JsonConvert(){
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

        public <T> T deSerializeObject(String content, TypeReference<T> trf){
            try {
                var fdata= this.objectMapper.<T>readValue(content,trf);
                return fdata;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
