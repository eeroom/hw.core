package io.github.eeroom.gtop.entity.hz;

import java.util.HashMap;

public class StartProcessInput {
    public BizType getBizType() {
        return bizType;
    }

    public void setBizType(BizType bizType) {
        this.bizType = bizType;
    }

    BizType bizType;


    public HashMap<String, Object> getFormdata() {
        return formdata;
    }

    public void setFormdata(HashMap<String, Object> formdata) {
        this.formdata = formdata;
    }

    HashMap<String,Object> formdata;
}