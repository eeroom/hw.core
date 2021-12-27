package io.github.eeroom.sf.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.eeroom.entity.sf.StartProcessInput;
import io.github.eeroom.entity.camunda.CompleteTaskInput;
import io.github.eeroom.entity.sf.kuaidi.EntityByPaymoney;
import io.github.eeroom.entity.sf.db.bizdata;
import io.github.eeroom.entity.sf.db.bizdatasub;
import io.github.eeroom.sf.JsonHelper;
import io.github.eeroom.sf.LoginUserInfo;
import io.github.eeroom.sf.SfDbContext;
import io.github.eeroom.sf.SkipAuthentication;
import io.github.eeroom.sf.bpm.CamundaBll;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class KuaidiController {
    CamundaBll bll;
    JsonHelper jsonHelper;
    SfDbContext dbContext;
    LoginUserInfo loginUserInfo;
    public KuaidiController(CamundaBll bll, JsonHelper jsonHelper, SfDbContext dbContext, LoginUserInfo loginUserInfo){
        this.bll=bll;
        this.jsonHelper=jsonHelper;
        this.dbContext=dbContext;
        this.loginUserInfo=loginUserInfo;
    }

    @SkipAuthentication
    public bizdata newTransfer(io.github.eeroom.entity.sf.kuaidi.EntityByCreate entity){
        StartProcessInput startProcessInput =new StartProcessInput();
        startProcessInput.setBizType(1);
        var jsonstr=this.jsonHelper.serializeObject(entity);
        var formdata=this.jsonHelper.deSerializeObject(jsonstr, new TypeReference<HashMap<String,Object>>() {});
        startProcessInput.setFormdata(formdata);
        this.loginUserInfo.setName(entity.getThirdpartId());
        return bll.startProcess(startProcessInput);
    }

    @SkipAuthentication
    public void completePaymoney(EntityByPaymoney entity){
        //必须是第三方自己名下的单
        var bizdata= this.dbContext.dbSet(bizdata.class)
                .select()
                .where(x->x.col(a->a.getprocessId()).eq(entity.getProcessInstanceId()))
                .firstOrDefault();
        var tmp= this.jsonHelper.deSerializeObject(bizdata.getcreateformdatajson(),io.github.eeroom.entity.sf.kuaidi.EntityByCreate.class);
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
        var jsonstr=this.jsonHelper.serializeObject(entity);
        var formdata=this.jsonHelper.deSerializeObject(jsonstr, new TypeReference<HashMap<String,Object>>() {});
        CompleteTaskInput completeTaskInput =new CompleteTaskInput();
        completeTaskInput.setTaskId(bizdatasub.gettaskId());
        completeTaskInput.setFormdata(formdata);
        this.loginUserInfo.setName(entity.getThirdpartId());
        bll.complete(completeTaskInput);
    }
}
