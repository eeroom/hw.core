package io.github.eeroom.hzoa.camunda;

import java.util.HashMap;

public class StartProcessInput {

    public String getProcdefKey() {
        return procdefKey;
    }

    public void setProcdefKey(String procdefKey) {
        this.procdefKey = procdefKey;
    }

    String procdefKey;


    public HashMap<String, Object> getFormdata() {
        return formdata;
    }

    public void setFormdata(HashMap<String, Object> formdata) {
        this.formdata = formdata;
    }

    HashMap<String,Object> formdata;
}