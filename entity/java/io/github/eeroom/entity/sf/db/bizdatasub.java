package io.github.eeroom.entity.sf.db;

public class bizdatasub {

    Integer id;

    public Integer getid() {
        return id;
    }

    public void setid(Integer pValue) {
        this.id = pValue;
    }

    String processId;

    public String getprocessId() {
        return processId;
    }

    public void setprocessId(String pValue) {
        this.processId = pValue;
    }

    String taskId;

    public String gettaskId() {
        return taskId;
    }

    public void settaskId(String pValue) {
        this.taskId = pValue;
    }

    String handlerId;

    public String gethandlerId() {
        return handlerId;
    }

    public void sethandlerId(String pValue) {
        this.handlerId = pValue;
    }

    public Integer getHandlerByMe() {
        return handlerByMe;
    }

    public void setHandlerByMe(Integer handlerByMe) {
        this.handlerByMe = handlerByMe;
    }

    Integer handlerByMe;



    String completeformdatajson;

    public String getcompleteformdatajson() {
        return completeformdatajson;
    }

    public void setcompleteformdatajson(String pValue) {
        this.completeformdatajson = pValue;
    }

    Integer taskstatus;

    public Integer gettaskstatus() {
        return taskstatus;
    }

    public void settaskstatus(Integer pValue) {
        this.taskstatus = pValue;
    }

}