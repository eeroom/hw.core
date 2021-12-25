package io.github.eeroom.hz.controller;

import io.github.eeroom.entity.sf.kuaidi.EntityByCreate;
import io.github.eeroom.entity.sf.kuaidi.EntityByPaymoney;
import io.github.eeroom.entity.sf.kuaidi.NoticeMessage;
import io.github.eeroom.entity.sf.kuaidi.NoticeResponse;
import io.github.eeroom.entity.sfdb.bizdata;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class KuaidiCallback implements io.github.eeroom.entity.sf.kuaidi.IKuaidiController {

    @Override
    public bizdata newTransfer(EntityByCreate entity) {
        throw new RuntimeException("不需要实现");
    }

    @Override
    public void completePaymoney(EntityByPaymoney entity) {
        throw new RuntimeException("不需要实现");
    }

    @Override
    public NoticeResponse notice(NoticeMessage msg) {
        //如果是通知过磅的结果，就推动外发快递的进入领导审批
        var rt=new NoticeResponse();
        rt.setCode(200);
        return rt;
    }
}
