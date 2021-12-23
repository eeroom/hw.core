package io.github.eeroom.entity.sf.kuaidi;

public class EntityByPaymoney {
    String processInstanceId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getThirdpartId() {
        return thirdpartId;
    }

    public void setThirdpartId(String thirdpartId) {
        this.thirdpartId = thirdpartId;
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

    String thirdpartId;
    Integer moneyCount;
    String payType;
}
