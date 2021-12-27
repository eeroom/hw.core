package io.github.eeroom.gtop.entity.sf;


import java.util.HashMap;

public class StartProcessInput {
    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    int bizType;


    public HashMap<String, Object> getFormdata() {
        return formdata;
    }

    public void setFormdata(HashMap<String, Object> formdata) {
        this.formdata = formdata;
    }

    HashMap<String,Object> formdata;
}
