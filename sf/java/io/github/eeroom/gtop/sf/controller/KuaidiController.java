package io.github.eeroom.gtop.sf.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.eeroom.gtop.api.sf.kuaidi.IKuaidiController;
import io.github.eeroom.gtop.entity.TaskStatus;
import io.github.eeroom.gtop.entity.camunda.CompleteTaskInput;
import io.github.eeroom.gtop.entity.camunda.StartProcessInput;
import io.github.eeroom.gtop.entity.sf.BizName;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByPaymoney;
import io.github.eeroom.gtop.entity.sf.db.bizdata;
import io.github.eeroom.gtop.entity.sf.db.bizdatasub;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByCreate;
import io.github.eeroom.gtop.sf.authen.CurrentUserInfo;
import io.github.eeroom.gtop.sf.MyDbContext;
import io.github.eeroom.gtop.sf.authen.SkipAuthentication;
import io.github.eeroom.gtop.sf.serialize.JsonConvert;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class KuaidiController implements IKuaidiController {
    JsonConvert jsonConvert;
    MyDbContext dbContext;
    CurrentUserInfo currentUserInfo;
    CamundaController camundaController;
    public KuaidiController(CamundaController camundaController, JsonConvert jsonConvert, MyDbContext dbContext, CurrentUserInfo currentUserInfo){
        this.camundaController=camundaController;
        this.jsonConvert = jsonConvert;
        this.dbContext=dbContext;
        this.currentUserInfo = currentUserInfo;
    }

    @SkipAuthentication
    @Override
    public bizdata create(EntityByCreate entity) {
        var startProcessInput =new StartProcessInput();
        startProcessInput.setBizName(BizName.国内快递.name());
        var jsonstr=this.jsonConvert.serializeObject(entity);
        var formdata=this.jsonConvert.deSerializeObject(jsonstr, new TypeReference<HashMap<String,Object>>() {});
        startProcessInput.setFormdata(formdata);
        this.currentUserInfo.setName(entity.getThirdpartId());
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
        if(!entityByCreate.getThirdpartId().equals(entityByPaymoney.getThirdpartId()))
            throw new RuntimeException("只能支付自己名下的快递");
        //这种场景下，对方系统只能提供流程单号，
        //需要我们自己反推找出taskid,
        var bizdatasub= this.dbContext.dbSet(bizdatasub.class).select()
                .where(x->x.col(a->a.getprocessId()).eq(entityByPaymoney.getProcessInstanceId()))
                .where(x->x.col(a->a.getassignee()).eq(entityByPaymoney.getThirdpartId()))
                .where(x->x.col(a->a.getstatus()).eq(TaskStatus.处理中))
                .firstOrDefault();
        if(bizdatasub==null)
            throw new RuntimeException("此流程的对方付款环节已关闭或者不存在,id:"+entityByPaymoney.getProcessInstanceId());
        var jsonstr=this.jsonConvert.serializeObject(entityByPaymoney);
        var formdata=this.jsonConvert.deSerializeObject(jsonstr, new TypeReference<HashMap<String,Object>>() {});
        CompleteTaskInput completeTaskInput =new CompleteTaskInput();
        completeTaskInput.setTaskId(bizdatasub.gettaskId());
        completeTaskInput.setFormdata(formdata);
        this.currentUserInfo.setName(entityByPaymoney.getThirdpartId());
        this.camundaController.complete(completeTaskInput);
    }
}
