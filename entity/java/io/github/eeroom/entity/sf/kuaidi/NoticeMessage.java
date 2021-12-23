package io.github.eeroom.entity.sf.kuaidi;

import java.util.HashMap;

public class NoticeMessage {
    public NoticeMessage(String processInstanceId, String type, HashMap<String,Object> data){
        this.type=type;
        this.data=data;
        this.processInstanceId=processInstanceId;
    }

    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    HashMap<String,Object> data;
    String processInstanceId;

}
