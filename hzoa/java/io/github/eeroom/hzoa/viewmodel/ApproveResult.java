package io.github.eeroom.hzoa.viewmodel;


import io.github.eeroom.hzoa.camunda.ApproveType;

public class ApproveResult {
    ApproveType approveType;
    Integer moneyCount;
    String payType;
    String taskId;
    String newHandler;

    public ApproveType getApproveType() {
        return approveType;
    }

    public void setApproveType(ApproveType approveType) {
        this.approveType = approveType;
    }

    public Integer getMoneyCount() {
        return moneyCount;
    }

    public void setMoneyCount(Integer moneyCount) {
        this.moneyCount = moneyCount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getNewHandler() {
        return newHandler;
    }

    public void setNewHandler(String newHandler) {
        this.newHandler = newHandler;
    }



}
