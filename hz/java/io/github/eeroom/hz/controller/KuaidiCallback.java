package io.github.eeroom.hz.controller;

import io.github.eeroom.entity.sf.kuaidi.NoticeResponse;

public class KuaidiCallback {
    public io.github.eeroom.entity.sf.kuaidi.NoticeResponse recive(io.github.eeroom.entity.sf.kuaidi.NoticeMessage msg){
        //如果是通知过磅的结果，就推动外发快递的进入领导审批
        var rt=new NoticeResponse();
        rt.setCode(200);
        return rt;
    }
}
