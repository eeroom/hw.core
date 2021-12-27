package io.github.eeroom.entity.camunda;

public enum ApproveResult {
    ok(1,"通过"),
    abort(2,"驳回"),
    deleget(3,"转审");

    int value;
    String name;
    private ApproveResult(int value, String name){
        this.value=value;
        this.name=name;
    }
}
