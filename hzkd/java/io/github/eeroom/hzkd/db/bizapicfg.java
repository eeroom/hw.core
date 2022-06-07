package io.github.eeroom.hzkd.db;


import io.github.eeroom.hzkd.viewmodel.ApiAlias;

public class bizapicfg {

    ApiAlias api;

    public ApiAlias getapi() {
        return api;
    }

    public void setapi(ApiAlias pValue) {
        this.api = pValue;
    }

    String procdefKey;

    public String getprocdefKey() {
        return procdefKey;
    }

    public void setprocdefKey(String pValue) {
        this.procdefKey = pValue;
    }

}