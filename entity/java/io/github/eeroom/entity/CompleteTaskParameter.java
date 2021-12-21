package io.github.eeroom.entity;

public class CompleteTaskParameter {
    String taskId;

    UserTaskResult result;

    public UserTaskResult getResult() {
        return result;
    }

    public void setResult(UserTaskResult result) {
        this.result = result;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCompleteformdatajson() {
        return completeformdatajson;
    }

    public void setCompleteformdatajson(String completeformdatajson) {
        this.completeformdatajson = completeformdatajson;
    }

    String completeformdatajson;

    public String getDelegetHandler() {
        return delegetHandler;
    }

    public void setDelegetHandler(String delegetHandler) {
        this.delegetHandler = delegetHandler;
    }

    String delegetHandler;
}