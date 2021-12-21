package io.github.eeroom.entity;

public class StartProcessParameter {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String key;

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
