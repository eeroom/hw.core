package io.github.eeroom.gtop.hz.controller;

import io.github.eeroom.gtop.api.sf.IKuaidiController;
import io.github.eeroom.gtop.entity.hz.TaskStatus;
import io.github.eeroom.gtop.entity.hz.db.bizdataex;
import io.github.eeroom.gtop.entity.hz.db.bizdatasub;
import io.github.eeroom.gtop.entity.sf.db.bizdata;
import io.github.eeroom.gtop.entity.sf.kuaidi.*;
import io.github.eeroom.gtop.hz.authen.SkipAuthentication;
import io.github.eeroom.gtop.hz.MyDbContext;
import io.github.eeroom.gtop.hz.MyObjectFacotry;
import io.github.eeroom.gtop.hz.camunda.VariableKey;
import io.github.eeroom.gtop.hz.camunda.ProcessInstanceHandler;
import io.github.eeroom.gtop.hz.serialize.JsonConvert;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class KuaidiCallback implements IKuaidiController {
    MyDbContext dbContext;
    ProcessInstanceHandler processInstanceHandler;
    public KuaidiCallback(MyDbContext dbContext, ProcessInstanceHandler processInstanceHandler){
        this.dbContext=dbContext;
        this.processInstanceHandler=processInstanceHandler;
    }

    @Override
    public bizdata newTransfer(EntityByCreate entity) {
        throw new RuntimeException("不需要实现");
    }

    @Override
    public void completePaymoney(EntityByPaymoney entity) {
        throw new RuntimeException("不需要实现");
    }

    @SkipAuthentication
    @Override
    public NoticeResponse notice(NoticeMessage msg) {
        //如果是通知过磅的结果，就推动外发快递的进入领导审批
        if(msg.getType().equals(NoticeMessageType.过磅)){
            //把对应的hz的流程实例找出来，找到当前的task,完成这个task
            var bizex= this.dbContext.dbSet(bizdataex.class)
                    .where(x->x.col(a->a.geteKey()).eq(VariableKey.processInstanceIdBySf))
                    .where(x->x.col(a->a.geteValue()).eq(msg.getProcessInstanceId()))
                    .firstOrDefault();
            if(bizex==null)
                throw new RuntimeException(String.format("没有对应的流程实例,sf流程id:%s",msg.getProcessInstanceId()));
            var bizdsub= this.dbContext.dbSet(bizdatasub.class)
                    .where(x->x.col(a->a.getprocessId()).eq(bizex.getprocessId()))
                    .where(x->x.col(a->a.getstatus()).eq(TaskStatus.处理中))
                    .where(x->x.col(a->a.getassignee()).eq("sf"))
                    .firstOrDefault();
            if(bizdsub==null)
                throw new RuntimeException(String.format("当前没有改任务需要处理:%s", MyObjectFacotry.getBean(JsonConvert.class).serializeObject(msg)));
            this.processInstanceHandler.complete(bizdsub.gettaskId(),msg.getData());
        }
        var rt=new NoticeResponse();
        rt.setCode(200);
        return rt;
    }
}
