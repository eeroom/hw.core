package io.github.eeroom.hzcore.hzoa.kd;

public class EntityByCreate {
    String reciver;

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

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    int senderCityId;
    int reciveCityId;
    String departmentCode;
}
