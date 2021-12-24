package io.github.eeroom.sf.bpm;

import io.github.eeroom.entity.BpmdataByNewProcess;
import io.github.eeroom.entity.BpmdataByUserTask;
import io.github.eeroom.entity.sfdb.bizdatasub;
import io.github.eeroom.entity.sfdb.biztype;
import io.github.eeroom.sf.JsonHelper;
import io.github.eeroom.sf.LoginUserInfo;
import io.github.eeroom.sf.SfDbContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.HashMap;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CamundaBll {
    org.camunda.bpm.engine.ProcessEngine processEngine;
    SfDbContext dbContext;
    JsonHelper jsonHelper;
    LoginUserInfo loginUserInfo;
    public CamundaBll(org.camunda.bpm.engine.ProcessEngine processEngine, SfDbContext dbContext, JsonHelper jsonHelper, LoginUserInfo loginUserInfo){
        this.processEngine=processEngine;
        this.loginUserInfo=loginUserInfo;
        this.dbContext=dbContext;
        this.jsonHelper=jsonHelper;
    }

    public io.github.eeroom.entity.sfdb.bizdata startProcess(BpmdataByNewProcess bpmdataByUserTask) {
        //根据业务id获取对应流程的key
        var btp= this.dbContext.dbSet(biztype.class).select()
                .where(x->x.col(a->a.getid()).eq(bpmdataByUserTask.getBizType()))
                .firstOrDefault();
        if(btp==null)
            throw new IllegalArgumentException("指定的业务类别不存在："+ bpmdataByUserTask.getBizType());
        var map=new HashMap<String,Object>();
        map.putAll(bpmdataByUserTask.getFormdata());
        //SfActions必须实现Serializable接口
        SfActions sfActions=new SfActions();
        //jdk11环境下，  如果camunda的流程参数使用javabean类型，就需要添加这个依赖。tomcat7和2.3版本的有小冲突，启动报错，但不影响使用，这里使用2.2版本，tomcat7启动不报错
        map.put("sfActions",sfActions);
        var processInstance = this.processEngine.getRuntimeService()
                .startProcessInstanceByKey(btp.getcamundaKey(),map);
        //往主业务表添加一条记录
        io.github.eeroom.entity.sfdb.bizdata biz=new io.github.eeroom.entity.sfdb.bizdata();
        biz.setbizType(bpmdataByUserTask.getBizType());
        biz.setcreateBy(this.loginUserInfo.getName());
        biz.setcreateformdatajson(this.jsonHelper.serializeObject(bpmdataByUserTask.getFormdata()));
        biz.setcreateTime(new java.sql.Timestamp(new Date().getTime()));
        biz.setprocessId(processInstance.getProcessInstanceId());
        var title= this.processEngine.getRuntimeService().getVariable(processInstance.getProcessInstanceId(),"bizdataTitle");
        if(title!=null){
            biz.settitle(title.toString());
        }
        this.dbContext.add(biz)
                .setInsertAllCol();
        this.dbContext.saveChange();
        return biz;
    }

    public  void  complete(BpmdataByUserTask completeTaskParameter)  {
        //前置校验，当前登录人确实是这个task的处理人
        var task= this.processEngine.getTaskService().createTaskQuery().taskId(completeTaskParameter.getTaskId())
                .singleResult();
        if(task==null)
            throw new IllegalArgumentException("指定id的task不存在，taskid="+completeTaskParameter.getTaskId());
        if(!this.loginUserInfo.getName().equals(task.getAssignee()))
            throw new IllegalArgumentException("你不是当前任务的处理人！");
        //map中的键和可能值，流程图和completeTaskParameter要事先约定好，
        // 这里的约定是，
        // 如果UserTask执行结果是修改当前任务的执行人，就在表单数据中包含userTaskResult：delegate和delegetedHandler：新的执行人
        String userTaskResult="userTaskResult";
        String delegetedHandler="delegetedHandler";
        if(completeTaskParameter.getFormdata().containsKey(userTaskResult) &&"delegate".equals(completeTaskParameter.getFormdata().get(userTaskResult))){
            if(completeTaskParameter.getFormdata().containsKey(delegetedHandler) || completeTaskParameter.getFormdata().get(delegetedHandler).toString().length()<1)
                throw new IllegalArgumentException("必须指定被委托人");
            this.processEngine.getTaskService().delegateTask(completeTaskParameter.getTaskId(),completeTaskParameter.getFormdata().get(delegetedHandler).toString());
        }else {
            this.processEngine.getTaskService().complete(completeTaskParameter.getTaskId(),completeTaskParameter.getFormdata());
        }
        //更新主业务表子表，把状态置为close
        //一个人任务，多人处理的情况下，当前人记录表单数据，其他人只更新taskstatus
        this.dbContext.edit(bizdatasub.class)
                .setUpdateCol(x->x.getcompleteformdatajson(),this.jsonHelper.serializeObject(completeTaskParameter.getFormdata()))
                .setUpdateCol(x->x.getHandlerByMe(),1)
                .where(x->x.col(a->a.gettaskId()).eq(completeTaskParameter.getTaskId()))
                .where(x->x.col(a->a.gettaskstatus()).eq(0))
                .where(x->x.col(a->a.gethandlerId()).eq(this.loginUserInfo.getName()));
        this.dbContext.edit(bizdatasub.class)
                .setUpdateCol(x->x.gettaskstatus(),1)
                .setUpdateCol(x->x.getcompleteformdatajson(),this.jsonHelper.serializeObject(completeTaskParameter.getFormdata()))
                .where(x->x.col(a->a.gettaskId()).eq(completeTaskParameter.getTaskId()))
                .where(x->x.col(a->a.gettaskstatus()).eq(0));
        this.dbContext.saveChange();
    }
}
