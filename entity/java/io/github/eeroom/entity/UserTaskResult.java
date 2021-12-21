package io.github.eeroom.entity;

public enum UserTaskResult {
    ok(0,"通过"),
    abort(1,"驳回"),
    deleget(2,"转审");

    int value;
    String name;
    private UserTaskResult(int value,String name){
        this.value=value;
        this.name=name;
    }
}
