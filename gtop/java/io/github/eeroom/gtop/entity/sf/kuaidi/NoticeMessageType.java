package io.github.eeroom.gtop.entity.sf.kuaidi;

public enum  NoticeMessageType {
    酒馆等待接收包裹("酒馆等待接收包裹"),
    酒馆已接收包裹("酒馆已接收包裹"),
    过磅("过磅"),
    发往集散中心("发往集散中心"),
    送货上门("送货上门");
    String value;
    private NoticeMessageType(String value){
        this.value=value;
    }

}
