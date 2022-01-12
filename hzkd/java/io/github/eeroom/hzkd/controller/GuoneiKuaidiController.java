package io.github.eeroom.hzkd.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.eeroom.hzcore.hzkd.api.IGuoneiKuaidiController;
import io.github.eeroom.hzcore.TaskStatus;
import io.github.eeroom.hzcore.camunda.CompleteTaskInput;
import io.github.eeroom.hzcore.camunda.StartProcessInput;
import io.github.eeroom.hzcore.hzkd.ApiAlias;
import io.github.eeroom.hzcore.hzkd.db.bizapicfg;
import io.github.eeroom.hzcore.hzkd.db.kuaidientcustomer;
import io.github.eeroom.hzcore.hzkd.guonei.EntityByPaymoney;
import io.github.eeroom.hzcore.hzkd.db.bizdata;
import io.github.eeroom.hzcore.hzkd.db.bizdatasub;
import io.github.eeroom.hzcore.hzkd.guonei.EntityByCreate;
import io.github.eeroom.hzkd.authen.CurrentUserInfo;
import io.github.eeroom.hzkd.MyDbContext;
import io.github.eeroom.hzkd.authen.SkipAuthentication;
import io.github.eeroom.hzkd.serialize.JsonConvert;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GuoneiKuaidiController implements IGuoneiKuaidiController {
    JsonConvert jsonConvert;
    MyDbContext dbContext;
    CurrentUserInfo currentUserInfo;
    CamundaController camundaController;
    public GuoneiKuaidiController(CamundaController camundaController, JsonConvert jsonConvert, MyDbContext dbContext, CurrentUserInfo currentUserInfo){
        this.camundaController=camundaController;
        this.jsonConvert = jsonConvert;
        this.dbContext=dbContext;
        this.currentUserInfo = currentUserInfo;
    }

    @SkipAuthentication
    @Override
    public bizdata create(EntityByCreate entity) {
        var apicfg= this.dbContext.dbSet(bizapicfg.class).select()
                .where(x->x.col(a->a.getapi()).eq(ApiAlias.GuoneiKuaidi))
                .firstOrDefault();
        if(apicfg==null)
            throw new RuntimeException(String.format("没有找到bizapicfg的数据,请联系管理员配置，api:%s",ApiAlias.GuoneiKuaidi.name()));
        //校验第三方，是否存在，是否有权限等等
        var customer= this.dbContext.dbSet(kuaidientcustomer.class).select()
                .where(x->x.col(a->a.getid()).eq(entity.getCustomerId()))
                .firstOrDefault();
        if(customer==null)
            throw new RuntimeException(String.format("指定的客户id不存在，id:%s",entity.getCustomerId()));
        var startProcessInput =new StartProcessInput();
        startProcessInput.setProcdefKey(apicfg.getprocdefKey());
        var jsonstr=this.jsonConvert.serializeObject(entity);
        var formdata=this.jsonConvert.deSerializeObject(jsonstr, new TypeReference<HashMap<String,Object>>() {});
        startProcessInput.setFormdata(formdata);
        this.currentUserInfo.setAccount(entity.getCustomerId());
        return camundaController.startProcess(startProcessInput);
    }

    @SkipAuthentication
    @Override
    public void paymoney(EntityByPaymoney entityByPaymoney) {
        //必须是第三方自己名下的单
        var bizdata= this.dbContext.dbSet(bizdata.class)
                .select()
                .where(x->x.col(a->a.getprocessId()).eq(entityByPaymoney.getProcessInstanceId()))
                .firstOrDefault();
        var entityByCreate= this.jsonConvert.deSerializeObject(bizdata.getcreateformdatajson(), EntityByCreate.class);
        if(!entityByCreate.getCustomerId().equals(entityByPaymoney.getCustomerId()))
            throw new RuntimeException("只能支付自己名下的快递");
        //这种场景下，对方系统只能提供流程单号，
        //需要我们自己反推找出taskid,
        var bizdatasub= this.dbContext.dbSet(bizdatasub.class).select()
                .where(x->x.col(a->a.getprocessId()).eq(entityByPaymoney.getProcessInstanceId()))
                .where(x->x.col(a->a.getassignee()).eq(entityByPaymoney.getCustomerId()))
                .where(x->x.col(a->a.getstatus()).eq(TaskStatus.处理中))
                .firstOrDefault();
        if(bizdatasub==null)
            throw new RuntimeException("此流程的对方付款环节已关闭或者不存在,id:"+entityByPaymoney.getProcessInstanceId());
        var jsonstr=this.jsonConvert.serializeObject(entityByPaymoney);
        var formdata=this.jsonConvert.deSerializeObject(jsonstr, new TypeReference<HashMap<String,Object>>() {});
        CompleteTaskInput completeTaskInput =new CompleteTaskInput();
        completeTaskInput.setTaskId(bizdatasub.gettaskId());
        completeTaskInput.setFormdata(formdata);
        this.currentUserInfo.setAccount(entityByPaymoney.getCustomerId());
        this.camundaController.complete(completeTaskInput);
    }
}
