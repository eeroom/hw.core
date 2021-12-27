package io.github.eeroom.gtop.entity.sf.kuaidi;

import java.util.HashMap;

public class FeedMessage {
    public FeedMessage(String processInstanceId, FeedType type, HashMap<String,Object> data){
        this.type=type;
        this.data=data;
        this.processInstanceId=processInstanceId;
    }

    FeedType type;

    public FeedType getType() {
        return type;
    }

    public void setType(FeedType type) {
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
