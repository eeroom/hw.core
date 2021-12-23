package io.github.eeroom.entity;

public class KuaidiCreateInput {
    String deptName;
    String sender;
    String reciver;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public int getSenderCityId() {
        return senderCityId;
    }

    public void setSenderCityId(int senderCityId) {
        this.senderCityId = senderCityId;
    }

    public int getReciveCityId() {
        return reciveCityId;
    }

    public void setReciveCityId(int reciveCityId) {
        this.reciveCityId = reciveCityId;
    }

    int senderCityId;
    int reciveCityId;
}
