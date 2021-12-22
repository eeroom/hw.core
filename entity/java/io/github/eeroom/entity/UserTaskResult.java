package io.github.eeroom.entity;

public enum UserTaskResult {
    ok(1,"通过"),
    abort(2,"驳回"),
    deleget(3,"转审");

    int value;
    String name;
    private UserTaskResult(int value,String name){
        this.value=value;
        this.name=name;
    }
}
