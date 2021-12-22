package io.github.eeroom.entity;

public class StartProcessParameter {
    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }


    int bizType;

    public String getCreateformdatajson() {
        return createformdatajson;
    }

    public void setCreateformdatajson(String createformdatajson) {
        this.createformdatajson = createformdatajson;
    }

    String createformdatajson;
}
