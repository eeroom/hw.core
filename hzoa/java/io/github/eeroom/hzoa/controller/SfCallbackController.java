package io.github.eeroom.hzoa.controller;

import io.github.eeroom.hzcore.hzkd.api.IGuoneiKuaidiCallback;
import io.github.eeroom.hzcore.camunda.CompleteTaskInput;
import io.github.eeroom.hzcore.TaskStatus;
import io.github.eeroom.hzcore.hzkd.guonei.FeedMessage;
import io.github.eeroom.hzcore.hzkd.guonei.FeedResponse;
import io.github.eeroom.hzcore.hzkd.guonei.FeedType;
import io.github.eeroom.hzcore.hzoa.db.bizdataex;
import io.github.eeroom.hzcore.hzoa.db.bizdatasub;
import io.github.eeroom.hzoa.authen.CurrentUserInfo;
import io.github.eeroom.hzoa.authen.SkipAuthentication;
import io.github.eeroom.hzoa.MyDbContext;
import io.github.eeroom.hzoa.MyObjectFacotry;
import io.github.eeroom.hzoa.camunda.VariableKey;
import io.github.eeroom.hzoa.serialize.JsonConvert;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class SfCallbackController implements IGuoneiKuaidiCallback {
    MyDbContext dbContext;
    CamundaController camundaController;
    CurrentUserInfo currentUserInfo;
    JsonConvert jsonConvert;
    public SfCallbackController(MyDbContext dbContext, CamundaController camundaController, CurrentUserInfo currentUserInfo,JsonConvert jsonConvert){
        this.dbContext=dbContext;
        this.camundaController = camundaController;
        this.currentUserInfo=currentUserInfo;
        this.jsonConvert=jsonConvert;
    }

    @SkipAuthentication
    @Override
    public FeedResponse execute(FeedMessage msg) {
        //如果是通知过磅的结果，就推动外发快递的进入领导审批
        if(msg.getType().equals(FeedType.过磅)){
            //参数校验，必须有重量信息
            var guobangrt= this.jsonConvert.deSerializeObject(this.jsonConvert.serializeObject(msg.getData()), io.github.eeroom.hzcore.hzkd.guonei.GuobangResult.class);
            if(guobangrt.getZhongliang()==null || guobangrt.getZhongliang()<0)
                throw new RuntimeException(String.format("过磅的信息异常，信息内容：%s",this.jsonConvert.serializeObject(msg.getData())));
            //把对应的hz的流程实例找出来，找到当前的task,完成这个task
            var bizex= this.dbContext.dbSet(bizdataex.class).select()
                    .where(x->x.col(a->a.geteKey()).eq(VariableKey.processInstanceIdOfSf))
                    .where(x->x.col(a->a.geteValue()).eq(msg.getProcessInstanceId()))
                    .firstOrDefault();
            if(bizex==null)
                throw new RuntimeException(String.format("没有对应的流程实例,sf流程id:%s",msg.getProcessInstanceId()));
            var bizdsub= this.dbContext.dbSet(bizdatasub.class).select()
                    .where(x->x.col(a->a.getprocessId()).eq(bizex.getprocessId()))
                    .where(x->x.col(a->a.getstatus()).eq(TaskStatus.处理中))
                    .where(x->x.col(a->a.getassignee()).eq(VariableKey.sf))
                    .firstOrDefault();
            if(bizdsub==null)
                throw new RuntimeException(String.format("当前没有改任务需要处理:%s", MyObjectFacotry.getBean(JsonConvert.class).serializeObject(msg)));
            var cp=new CompleteTaskInput();
            cp.setTaskId(bizdsub.gettaskId());
            cp.setFormdata(msg.getData());
            this.currentUserInfo.setAccount(bizdsub.getassignee());
            this.camundaController.complete(cp);
        }
        var rt=new FeedResponse();
        rt.setCode(200);
        return rt;
    }
}
