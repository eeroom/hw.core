package io.github.eeroom.gtop.entity.hz.db;

import io.github.eeroom.gtop.entity.hz.*;
import io.github.eeroom.gtop.entity.hz.BizType;

public class bizdata {

    String processId;

    public String getprocessId() {
        return processId;
    }

    public void setprocessId(String pValue) {
        this.processId = pValue;
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

    Integer status;

    public Integer getstatus() {
        return status;
    }

    public void setstatus(Integer pValue) {
        this.status = pValue;
    }

    BizType bizType;

    public BizType getbizType() {
        return bizType;
    }

    public void setbizType(BizType pValue) {
        this.bizType = pValue;
    }

}