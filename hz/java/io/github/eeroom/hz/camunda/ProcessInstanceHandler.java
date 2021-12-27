package io.github.eeroom.hz.camunda;

import io.github.eeroom.entity.hz.db.bizdata;
import io.github.eeroom.entity.hz.db.bizdatasub;
import io.github.eeroom.entity.hz.db.biztypeex;
import io.github.eeroom.hz.MyDbContext;
import io.github.eeroom.hz.MyObjectFacotry;
import io.github.eeroom.hz.authen.CurrentUserInfo;
import io.github.eeroom.hz.serialize.JsonConvert;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.HashMap;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ProcessInstanceHandler {
    org.camunda.bpm.engine.ProcessEngine processEngine;
    MyDbContext dbContext;
    JsonConvert jsonHelper;
    CurrentUserInfo currentUserInfo;
    public ProcessInstanceHandler(org.camunda.bpm.engine.ProcessEngine processEngine, MyDbContext dbContext, JsonConvert jsonHelper, CurrentUserInfo loginUserInfo){
        this.processEngine=processEngine;
        this.currentUserInfo =loginUserInfo;
        this.dbContext=dbContext;
        this.jsonHelper=jsonHelper;
    }

    public bizdata startProcess(biztypeex btpex, HashMap<String,Object> formdata) {
        var map=new HashMap<String,Object>();
        map.putAll(formdata);
        var formdataOfCreate= this.jsonHelper.serializeObject(formdata);
        map.put(VariableKey.formdataOfCreate,formdataOfCreate);
        var listenerHandler=new ListenerHandler();
        //jdk11环境下，  如果camunda的流程参数使用javabean类型，就需要添加这个依赖。tomcat7和2.3版本的有小冲突，启动报错，但不影响使用，这里使用2.2版本，tomcat7启动不报错
        map.put(VariableKey.listenerHandler,listenerHandler);
        var handlerKey=btpex.getprocdefineKey()+"Handler";
        if(MyObjectFacotry.containsBean(handlerKey)){
            //流程各自的handler
            map.put(handlerKey,MyObjectFacotry.getBean(handlerKey));
        }
        var processInstance = this.processEngine.getRuntimeService()
                .startProcessInstanceByKey(btpex.getprocdefineKey(),map);
        //往主业务表添加一条记录
        bizdata biz=new bizdata();
        biz.setbizType(btpex.getbizType());
        biz.setcreateBy(this.currentUserInfo.getAccount());
        biz.setcreateformdatajson(formdataOfCreate);
        biz.setcreateTime(new java.sql.Timestamp(new Date().getTime()));
        biz.setprocessId(processInstance.getProcessInstanceId());
        var title= this.processEngine.getRuntimeService().getVariable(processInstance.getProcessInstanceId(),VariableKey.bizdataTitle);
        if(title!=null){
            biz.settitle(title.toString());
        }
        this.dbContext.add(biz)
                .setInsertAllCol();
        this.dbContext.saveChange();
        return biz;
    }

    public  void  complete(String taskId,HashMap<String,Object> formdata)  {
        //前置校验，当前登录人确实是这个task的处理人
        var task= this.processEngine.getTaskService().createTaskQuery().taskId(taskId)
                .singleResult();
        if(task==null)
            throw new IllegalArgumentException("指定id的task不存在，taskid="+taskId);
        if(!this.currentUserInfo.getName().equals(task.getAssignee()))
            throw new IllegalArgumentException("你不是当前任务的处理人！");
        //map中的键和可能值，流程图和completeTaskParameter要事先约定好，
        // 这里的约定是，
        // 如果UserTask执行结果是修改当前任务的执行人，就在表单数据中包含completeResult：delegate和delegetedHandler：新的执行人
        String completeResult="completeResult";
        String delegetedHandler="delegetedHandler";
        String formdataOfComplete=this.jsonHelper.serializeObject(formdata);
        if(formdata.containsKey(completeResult) &&"delegate".equals(formdata.get(completeResult))){
            if(!formdata.containsKey(delegetedHandler))
                throw new IllegalArgumentException("必须指定被委托人");
            var tmpHanlder= formdata.get(delegetedHandler);
            if(tmpHanlder==null || tmpHanlder.toString().length()<1)
                throw new IllegalArgumentException("必须指定被委托人");
            this.processEngine.getTaskService().delegateTask(taskId,tmpHanlder.toString());
        }else {
            formdata.put(VariableKey.formdataOfComplete,formdataOfComplete);
            this.processEngine.getTaskService().complete(taskId,formdata);
        }
        //更新主业务表子表，把状态置为close
        //一个人任务，多人处理的情况下，当前人记录表单数据，其他人只更新taskstatus
        this.dbContext.edit(bizdatasub.class)
                .setUpdateCol(x->x.getcompleteformdatajson(),formdataOfComplete)
                .setUpdateCol(x->x.gethandlerByMe(),1)
                .where(x->x.col(a->a.gettaskId()).eq(taskId))
                .where(x->x.col(a->a.gethandlerId()).eq(this.currentUserInfo.getAccount()));
        this.dbContext.edit(bizdatasub.class)
                .setUpdateCol(x->x.gettaskstatus(),1)
                .where(x->x.col(a->a.gettaskId()).eq(taskId))
                .where(x->x.col(a->a.gettaskstatus()).eq(0));
        this.dbContext.saveChange();
    }
}
