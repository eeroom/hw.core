package io.github.eeroom.entity;

public class CompleteTaskParameter {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    String id;

    String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    String tag;
}