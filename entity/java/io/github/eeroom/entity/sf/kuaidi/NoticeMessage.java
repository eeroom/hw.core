package io.github.eeroom.entity.sf.kuaidi;

import java.util.HashMap;

public class NoticeMessage {
    public NoticeMessage(String processInstanceId, NoticeMessageType type, HashMap<String,Object> data){
        this.type=type;
        this.data=data;
        this.processInstanceId=processInstanceId;
    }

    NoticeMessageType type;

    public NoticeMessageType getType() {
        return type;
    }

    public void setType(NoticeMessageType type) {
        this.type = type;
    }


    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    HashMap<String,Object> data;
    String processInstanceId;

}
