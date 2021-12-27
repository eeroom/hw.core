package io.github.eeroom.entity.camunda;

import java.util.HashMap;

public class CompleteTaskInput {
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public HashMap<String, Object> getFormdata() {
        return formdata;
    }

    public void setFormdata(HashMap<String, Object> formdata) {
        this.formdata = formdata;
    }

    String taskId;
    HashMap<String,Object> formdata;
}
