package io.github.eeroom.hzcore.hzoa.db;


 import io.github.eeroom.hzcore.BizDataStatus;

public class bizdata {

    String processId;

    public String getprocessId() {
        return processId;
    }

    public void setprocessId(String pValue) {
        this.processId = pValue;
    }

    String procdefKey;

    public String getprocdefKey() {
        return procdefKey;
    }

    public void setprocdefKey(String pValue) {
        this.procdefKey = pValue;
    }

    String bizType;

    public String getbizType() {
        return bizType;
    }

    public void setbizType(String pValue) {
        this.bizType = pValue;
    }

    String title;

    public String gettitle() {
        return title;
    }

    public void settitle(String pValue) {
        this.title = pValue;
    }

    String createBy;

    public String getcreateBy() {
        return createBy;
    }

    public void setcreateBy(String pValue) {
        this.createBy = pValue;
    }

    java.sql.Timestamp createTime;

    public java.sql.Timestamp getcreateTime() {
        return createTime;
    }

    public void setcreateTime(java.sql.Timestamp pValue) {
        this.createTime = pValue;
    }

    String createformdatajson;

    public String getcreateformdatajson() {
        return createformdatajson;
    }

    public void setcreateformdatajson(String pValue) {
        this.createformdatajson = pValue;
    }

    BizDataStatus status;

    public BizDataStatus getstatus() {
        return status;
    }

    public void setstatus(BizDataStatus pValue) {
        this.status = pValue;
    }

    String createformComponentName;

    public String getcreateformComponentName() {
        return createformComponentName;
    }

    public void setcreateformComponentName(String pValue) {
        this.createformComponentName = pValue;
    }

    String completeformComponetName;

    public String getcompleteformComponetName() {
        return completeformComponetName;
    }

    public void setcompleteformComponetName(String pValue) {
        this.completeformComponetName = pValue;
    }

    String ico;

    public String getico() {
        return ico;
    }

    public void setico(String pValue) {
        this.ico = pValue;
    }

}