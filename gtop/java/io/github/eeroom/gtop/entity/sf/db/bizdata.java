package io.github.eeroom.gtop.entity.sf.db;

import io.github.eeroom.gtop.entity.*;
public class bizdata {

    String processId;

    public String getprocessId() {
        return processId;
    }

    public void setprocessId(String pValue) {
        this.processId = pValue;
    }

    String bizName;

    public String getbizName() {
        return bizName;
    }

    public void setbizName(String pValue) {
        this.bizName = pValue;
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

}