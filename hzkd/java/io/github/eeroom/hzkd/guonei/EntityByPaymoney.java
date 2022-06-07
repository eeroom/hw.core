package io.github.eeroom.hzkd.guonei;

public class EntityByPaymoney {
    String processInstanceId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Integer getMoneyCount() {
        return moneyCount;
    }

    public void setMoneyCount(Integer moneyCount) {
        this.moneyCount = moneyCount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    Integer moneyCount;
    String payType;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    String customerId;
}
