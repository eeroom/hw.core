package io.github.eeroom.hzcore.hzkd.guonei;

public class EntityByCreate {
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    String customerId;
    String sender;
    String reciver;
    int senderCityId;
    int reciveCityId;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public int getSenderCityId() {
        return senderCityId;
    }

    public void setSenderCityId(int senderCityId) {
        this.senderCityId = senderCityId;
    }

    public int getReciveCityId() {
        return reciveCityId;
    }

    public void setReciveCityId(int reciveCityId) {
        this.reciveCityId = reciveCityId;
    }
    


}
