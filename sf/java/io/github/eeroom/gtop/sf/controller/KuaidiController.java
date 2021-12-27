package io.github.eeroom.gtop.sf.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.eeroom.gtop.entity.sf.StartProcessInput;
import io.github.eeroom.gtop.entity.camunda.CompleteTaskInput;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByPaymoney;
import io.github.eeroom.gtop.entity.sf.db.bizdata;
import io.github.eeroom.gtop.entity.sf.db.bizdatasub;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByCreate;
import io.github.eeroom.gtop.sf.authen.CurrentUserInfo;
import io.github.eeroom.gtop.sf.MyDbContext;
import io.github.eeroom.gtop.sf.authen.SkipAuthentication;
import io.github.eeroom.gtop.sf.camunda.ProcessInstanceHandler;
import io.github.eeroom.gtop.sf.serialize.JsonConvert;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class KuaidiController {
    ProcessInstanceHandler bll;
    JsonConvert jsonConvert;
    MyDbContext dbContext;
    CurrentUserInfo currentUserInfo;
    public KuaidiController(ProcessInstanceHandler bll, JsonConvert jsonConvert, MyDbContext dbContext, CurrentUserInfo currentUserInfo){
        this.bll=bll;
        this.jsonConvert = jsonConvert;
        this.dbContext=dbContext;
        this.currentUserInfo = currentUserInfo;
    }

    @SkipAuthentication
    public bizdata newTransfer(EntityByCreate entity){
        StartProcessInput startProcessInput =new StartProcessInput();
        startProcessInput.setBizType(1);
        var jsonstr=this.jsonConvert.serializeObject(entity);
        var formdata=this.jsonConvert.deSerializeObject(jsonstr, new TypeReference<HashMap<String,Object>>() {});
        startProcessInput.setFormdata(formdata);
        this.currentUserInfo.setName(entity.getThirdpartId());
        return bll.startProcess(startProcessInput);
    }

    @SkipAuthentication
    public void completePaymoney(EntityByPaymoney entity){
        //必须是第三方自己名下的单
        var bizdata= this.dbContext.dbSet(bizdata.class)
                .select()
                .where(x->x.col(a->a.getprocessId()).eq(entity.getProcessInstanceId()))
                .firstOrDefault();
        var tmp= this.jsonConvert.deSerializeObject(bizdata.getcreateformdatajson(), EntityByCreate.class);
        if(!tmp.getThirdpartId().equals(entity.getThirdpartId()))
            throw new RuntimeException("只能支付自己名下的快递");
        //这种场景下，对方系统只能提供流程单号，
        //需要我们自己反推找出taskid,
        var bizdatasub= this.dbContext.dbSet(bizdatasub.class).select()
                .where(x->x.col(a->a.getprocessId()).eq(entity.getProcessInstanceId()))
                .where(x->x.col(a->a.gethandlerId()).eq(entity.getThirdpartId()))
                .where(x->x.col(a->a.gettaskstatus()).eq(0))
                .firstOrDefault();
        if(bizdatasub==null)
            throw new RuntimeException("此流程的对方付款环节已关闭或者不存在,id:"+entity.getProcessInstanceId());
        var jsonstr=this.jsonConvert.serializeObject(entity);
        var formdata=this.jsonConvert.deSerializeObject(jsonstr, new TypeReference<HashMap<String,Object>>() {});
        CompleteTaskInput completeTaskInput =new CompleteTaskInput();
        completeTaskInput.setTaskId(bizdatasub.gettaskId());
        completeTaskInput.setFormdata(formdata);
        this.currentUserInfo.setName(entity.getThirdpartId());
        bll.complete(completeTaskInput);
    }
}
