package io.github.eeroom.gtop.sf.controller;

import io.github.eeroom.gtop.entity.BizDataStatus;
import io.github.eeroom.gtop.entity.TaskStatus;
import io.github.eeroom.gtop.entity.camunda.StartProcessInput;
import io.github.eeroom.gtop.entity.camunda.CompleteTaskInput;
import io.github.eeroom.gtop.entity.sf.db.bizdata;
import io.github.eeroom.gtop.entity.sf.db.bizdatasub;
import io.github.eeroom.gtop.entity.sf.db.procdefex;
import io.github.eeroom.gtop.sf.MyDbContext;
import io.github.eeroom.gtop.sf.MyObjectFacotry;
import io.github.eeroom.gtop.sf.aspnet.HttpPost;
import io.github.eeroom.gtop.sf.authen.CurrentUserInfo;
import io.github.eeroom.gtop.sf.authen.SkipAuthentication;
import io.github.eeroom.gtop.sf.camunda.ListenerHandler;
import io.github.eeroom.gtop.sf.camunda.VariableKey;
import io.github.eeroom.gtop.sf.serialize.JsonConvert;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CamundaController {
    org.camunda.bpm.engine.ProcessEngine processEngine;

    CurrentUserInfo currentUserInfo;

    MyDbContext dbContext;
    JsonConvert jsonConvert;
    public CamundaController(org.camunda.bpm.engine.ProcessEngine processEngine, CurrentUserInfo currentUserInfo, MyDbContext dbContext,
                             JsonConvert jsonConvert){
        this.processEngine=processEngine;
        this.currentUserInfo = currentUserInfo;
        this.dbContext=dbContext;
        this.jsonConvert = jsonConvert;
    }


    @HttpPost
    @SkipAuthentication
    public boolean deploy(MultipartFile bpmXml,MultipartFile bpmPng) throws Throwable {
        var dlm = this.processEngine.getRepositoryService().createDeployment()
                .addInputStream(bpmXml.getOriginalFilename(),bpmXml.getInputStream())
                .addInputStream(bpmPng.getOriginalFilename(),bpmPng.getInputStream())
                .deploy();
        return true;
    }

    @HttpPost
    public List<?> getProcessDefinitionEntity()  {
        var lstProcessDefinition = this.processEngine.getRepositoryService().createProcessDefinitionQuery()
                .latestVersion()
                .orderByProcessDefinitionKey()
                .asc()
                .list()
                .stream()
                .map(x -> new ProcessDefinition() {
                    @Override
                    public String getDescription() {
                        return x.getDescription();
                    }

                    @Override
                    public boolean hasStartFormKey() {
                        return x.hasStartFormKey();
                    }

                    @Override
                    public boolean isSuspended() {
                        return x.isSuspended();
                    }

                    @Override
                    public String getVersionTag() {
                        return x.getVersionTag();
                    }

                    @Override
                    public String getId() {
                        return x.getId();
                    }

                    @Override
                    public String getCategory() {
                        return x.getCategory();
                    }

                    @Override
                    public String getName() {
                        return x.getName();
                    }

                    @Override
                    public String getKey() {
                        return x.getKey();
                    }

                    @Override
                    public int getVersion() {
                        return x.getVersion();
                    }

                    @Override
                    public String getResourceName() {
                        return x.getResourceName();
                    }

                    @Override
                    public String getDeploymentId() {
                        return x.getDeploymentId();
                    }

                    @Override
                    public String getDiagramResourceName() {
                        return x.getDiagramResourceName();
                    }

                    @Override
                    public String getTenantId() {
                        return x.getTenantId();
                    }

                    @Override
                    public Integer getHistoryTimeToLive() {
                        return x.getHistoryTimeToLive();
                    }
                })
                .collect(java.util.stream.Collectors.toList());
        return lstProcessDefinition;
    }

    public bizdata startProcess(StartProcessInput startProcessInput) {
        var procdefex= this.dbContext.dbSet(procdefex.class).select()
                .where(x->x.col(a->a.getprocdefKey()).eq(startProcessInput.getProcdefKey()))
                .firstOrDefault();
        if(procdefex==null)
            throw new RuntimeException(String.format("指定的prodefKey没有procdefEx的数据，请配置好procdefEx数据再填写申请单,prodefKey:%s", startProcessInput.getProcdefKey()));
        var map=new HashMap<String,Object>();
        map.putAll(startProcessInput.getFormdata());
        var formdataOfCreate= this.jsonConvert.serializeObject(startProcessInput.getFormdata());
        map.put(VariableKey.formdataOfCreate,formdataOfCreate);
        //jdk11环境下，  如果camunda的流程参数使用javabean类型，就需要添加这个依赖。tomcat7和2.3版本的有小冲突，启动报错，但不影响使用，这里使用2.2版本，tomcat7启动不报错
        var lstbeanName= MyObjectFacotry.getBeanNamesForType(ListenerHandler.class);
        //默认约定，如果有注册 流程图key对应的Handler,则使用这个handler,否则使用通用的ListenerHandler;
        //业务专用Handler直接继承通用的ListenerHandler，得到几个通用方法
        var handlerKey=procdefex.getprocdefKey()+"Handler";
        var beanName= Arrays.stream(lstbeanName).filter(x->x.equalsIgnoreCase(handlerKey)).findFirst();
        var listenerHandler=beanName.isPresent()?MyObjectFacotry.getBean(beanName.get()):MyObjectFacotry.getBean(VariableKey.listenerHandler);
        map.put(VariableKey.listenerHandler,listenerHandler);
        var processInstance = this.processEngine.getRuntimeService()
                .startProcessInstanceByKey(procdefex.getprocdefKey(),map);
        //往主业务表添加一条记录
        var biz=new io.github.eeroom.gtop.entity.sf.db.bizdata();
        biz.setprocdefKey(procdefex.getprocdefKey());
        biz.setbizType(procdefex.getbizType());
        biz.setcreateBy(this.currentUserInfo.getAccount());
        biz.setcreateformdatajson(formdataOfCreate);
        biz.setcreateTime(new java.sql.Timestamp(new Date().getTime()));
        biz.setprocessId(processInstance.getProcessInstanceId());
        biz.setcompleteformComponetName(procdefex.getcompleteformComponetName());
        biz.setcreateformComponentName(procdefex.getcreateformComponentName());
        biz.setico(procdefex.getico());
        biz.setstatus(BizDataStatus.处理中);
        var title=startProcessInput.getFormdata().getOrDefault(VariableKey.bizdataTitle,"");
        if("".equals(title)){
            title= this.processEngine.getRuntimeService().getVariable(processInstance.getProcessInstanceId(),VariableKey.bizdataTitle);
        }
        if(title!=null){
            biz.settitle(title.toString());
        }
        this.dbContext.add(biz)
                .setInsertAllCol();
        this.dbContext.saveChange();
        return biz;
    }

    public List<?> getUserTask(){
        var lsttask= this.processEngine.getTaskService().createTaskQuery()
                .taskAssignee(this.currentUserInfo.getName())
                .list()
                .stream()
                .map(x->new Task() {
                    @Override
                    public String getId() {
                        return x.getId();
                    }

                    @Override
                    public String getName() {
                        return  x.getName();
                    }

                    @Override
                    public void setName(String s) {

                    }

                    @Override
                    public String getDescription() {
                        return  x.getDescription();
                    }

                    @Override
                    public void setDescription(String s) {

                    }

                    @Override
                    public int getPriority() {
                        return  x.getPriority();
                    }

                    @Override
                    public void setPriority(int i) {

                    }

                    @Override
                    public String getOwner() {
                        return  x.getOwner();
                    }

                    @Override
                    public void setOwner(String s) {

                    }

                    @Override
                    public String getAssignee() {
                        return  x.getAssignee();
                    }

                    @Override
                    public void setAssignee(String s) {

                    }

                    @Override
                    public DelegationState getDelegationState() {
                        return  x.getDelegationState();
                    }

                    @Override
                    public void setDelegationState(DelegationState delegationState) {

                    }

                    @Override
                    public String getProcessInstanceId() {
                        return  x.getProcessInstanceId();
                    }

                    @Override
                    public String getExecutionId() {
                        return  x.getExecutionId();
                    }

                    @Override
                    public String getProcessDefinitionId() {
                        return x.getProcessDefinitionId();
                    }

                    @Override
                    public String getCaseInstanceId() {
                        return x.getCaseInstanceId();
                    }

                    @Override
                    public void setCaseInstanceId(String s) {

                    }

                    @Override
                    public String getCaseExecutionId() {
                        return x.getCaseExecutionId();
                    }

                    @Override
                    public String getCaseDefinitionId() {
                        return x.getCaseDefinitionId();
                    }

                    @Override
                    public Date getCreateTime() {
                        return x.getCreateTime();
                    }

                    @Override
                    public String getTaskDefinitionKey() {
                        return x.getTaskDefinitionKey();
                    }

                    @Override
                    public Date getDueDate() {
                        return x.getDueDate();
                    }

                    @Override
                    public void setDueDate(Date date) {

                    }

                    @Override
                    public Date getFollowUpDate() {
                        return x.getFollowUpDate();
                    }

                    @Override
                    public void setFollowUpDate(Date date) {

                    }

                    @Override
                    public void delegate(String s) {

                    }

                    @Override
                    public void setParentTaskId(String s) {

                    }

                    @Override
                    public String getParentTaskId() {
                        return x.getId();
                    }

                    @Override
                    public boolean isSuspended() {
                        return x.isSuspended();
                    }

                    @Override
                    public String getFormKey() {
                        return x.getId();
                    }

                    @Override
                    public String getTenantId() {
                        return x.getId();
                    }

                    @Override
                    public void setTenantId(String s) {

                    }
                })
                .collect(java.util.stream.Collectors.toList());
        return lsttask;
    }

    public  void  complete(CompleteTaskInput completeTaskInput)  {
        //前置校验，当前登录人确实是这个task的处理人
        var task= this.processEngine.getTaskService().createTaskQuery().taskId(completeTaskInput.getTaskId())
                .singleResult();
        if(task==null)
            throw new RuntimeException("指定id的task不存在，taskid="+completeTaskInput.getTaskId());
        if(this.currentUserInfo.getAccount()==null||!this.currentUserInfo.getAccount().equals(task.getAssignee()))
            throw new RuntimeException("你不是当前任务的处理人！account:"+this.currentUserInfo.getAccount());
        //map中的键和可能值，流程图和completeTaskParameter要事先约定好，
        // 这里的约定是，
        // 如果UserTask执行结果是修改当前任务的执行人，就在表单数据中包含completeResult：delegate和delegetedHandler：新的执行人
        String completeResult="completeResult";
        String delegetedHandler="delegetedHandler";
        var formdata=completeTaskInput.getFormdata();
        String formdataOfComplete=this.jsonConvert.serializeObject(formdata);
        if("delegate".equals(formdata.getOrDefault(completeResult,""))){
            var delegetedHandlerValue= formdata.getOrDefault(delegetedHandler,"").toString();
            if(delegetedHandlerValue==null || delegetedHandlerValue.length()<1)
                throw new RuntimeException("必须指定被委托人");
            this.processEngine.getTaskService().delegateTask(completeTaskInput.getTaskId(),delegetedHandlerValue);
        }else {
            formdata.put(VariableKey.formdataOfComplete,formdataOfComplete);
            this.processEngine.getTaskService().complete(completeTaskInput.getTaskId(),formdata);
        }
        //更新主业务表子表，把状态置为close
        //一个人任务，多人处理的情况下，当前人记录表单数据，其他人只更新taskstatus
        this.dbContext.edit(bizdatasub.class)
                .setUpdateCol(x->x.getcompleteformdatajson(),formdataOfComplete)
                .setUpdateCol(x->x.getassigneeCompleted(), TaskStatus.已完成)
                .where(x->x.col(a->a.gettaskId()).eq(completeTaskInput.getTaskId()))
                .where(x->x.col(a->a.getassignee()).eq(this.currentUserInfo.getAccount()));
        this.dbContext.edit(bizdatasub.class)
                .setUpdateCol(x->x.getstatus(),TaskStatus.已完成)
                .where(x->x.col(a->a.gettaskId()).eq(completeTaskInput.getTaskId()))
                .where(x->x.col(a->a.getstatus()).eq(TaskStatus.处理中));
        this.dbContext.saveChange();
    }

}
