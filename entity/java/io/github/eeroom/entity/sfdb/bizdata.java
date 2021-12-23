package io.github.eeroom.entity.sfdb;

public class bizdata {

    String processId;

    public String getprocessId() {
        return processId;
    }

    public void setprocessId(String pValue) {
        this.processId = pValue;
    }

    Integer bizType;

    public Integer getbizType() {
        return bizType;
    }

    public void setbizType(Integer pValue) {
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


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    int status;

}