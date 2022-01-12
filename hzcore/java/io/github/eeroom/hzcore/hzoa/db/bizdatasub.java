package io.github.eeroom.hzcore.hzoa.db;


 import io.github.eeroom.hzcore.TaskStatus;

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

    String assignee;

    public String getassignee() {
        return assignee;
    }

    public void setassignee(String pValue) {
        this.assignee = pValue;
    }

    TaskStatus assigneeCompleted;

    public TaskStatus getassigneeCompleted() {
        return assigneeCompleted;
    }

    public void setassigneeCompleted(TaskStatus pValue) {
        this.assigneeCompleted = pValue;
    }

    String completeformdatajson;

    public String getcompleteformdatajson() {
        return completeformdatajson;
    }

    public void setcompleteformdatajson(String pValue) {
        this.completeformdatajson = pValue;
    }

    TaskStatus status;

    public TaskStatus getstatus() {
        return status;
    }

    public void setstatus(TaskStatus pValue) {
        this.status = pValue;
    }

}