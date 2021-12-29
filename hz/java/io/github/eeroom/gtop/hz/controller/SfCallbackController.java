package io.github.eeroom.gtop.hz.controller;

import io.github.eeroom.gtop.api.sf.kuaidi.IGuoneiKuaidiCallback;
import io.github.eeroom.gtop.entity.camunda.CompleteTaskInput;
import io.github.eeroom.gtop.entity.TaskStatus;
import io.github.eeroom.gtop.entity.hz.db.bizdataex;
import io.github.eeroom.gtop.entity.hz.db.bizdatasub;
import io.github.eeroom.gtop.entity.sf.kuaidi.FeedMessage;
import io.github.eeroom.gtop.entity.sf.kuaidi.FeedResponse;
import io.github.eeroom.gtop.entity.sf.kuaidi.FeedType;
import io.github.eeroom.gtop.hz.authen.CurrentUserInfo;
import io.github.eeroom.gtop.hz.authen.SkipAuthentication;
import io.github.eeroom.gtop.hz.MyDbContext;
import io.github.eeroom.gtop.hz.MyObjectFacotry;
import io.github.eeroom.gtop.hz.camunda.VariableKey;
import io.github.eeroom.gtop.hz.serialize.JsonConvert;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class SfCallbackController implements IGuoneiKuaidiCallback {
    MyDbContext dbContext;
    CamundaController camundaController;
    CurrentUserInfo currentUserInfo;
    public SfCallbackController(MyDbContext dbContext, CamundaController camundaController, CurrentUserInfo currentUserInfo){
        this.dbContext=dbContext;
        this.camundaController = camundaController;
        this.currentUserInfo=currentUserInfo;
    }

    @SkipAuthentication
    @Override
    public FeedResponse execute(FeedMessage msg) {
        //如果是通知过磅的结果，就推动外发快递的进入领导审批
        if(msg.getType().equals(FeedType.过磅)){
            //把对应的hz的流程实例找出来，找到当前的task,完成这个task
            var bizex= this.dbContext.dbSet(bizdataex.class)
                    .where(x->x.col(a->a.geteKey()).eq(VariableKey.processInstanceIdOfSf))
                    .where(x->x.col(a->a.geteValue()).eq(msg.getProcessInstanceId()))
                    .firstOrDefault();
            if(bizex==null)
                throw new RuntimeException(String.format("没有对应的流程实例,sf流程id:%s",msg.getProcessInstanceId()));
            var bizdsub= this.dbContext.dbSet(bizdatasub.class)
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
