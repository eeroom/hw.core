package io.github.eeroom.sf.controller;

import io.github.eeroom.entity.CompleteTaskParameter;
import io.github.eeroom.entity.DeployAdd;
import io.github.eeroom.entity.StartProcessParameter;
import io.github.eeroom.entity.UserTaskResult;
import io.github.eeroom.sf.HttpPost;
import io.github.eeroom.sf.LoginUserInfo;
import io.github.eeroom.sf.SfDbContext;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import io.github.eeroom.entity.sfdb.*;
@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CamundaController {
    org.camunda.bpm.engine.ProcessEngine processEngine;

    LoginUserInfo loginUserInfo;

    SfDbContext dbContext;

    public CamundaController(org.camunda.bpm.engine.ProcessEngine processEngine, LoginUserInfo loginUserInfo, SfDbContext dbContext){
        this.processEngine=processEngine;
        this.loginUserInfo=loginUserInfo;
        this.dbContext=dbContext;
    }


    @HttpPost
    public boolean deploy(MultipartFile bpmnFile) throws Throwable {
        var dlm = this.processEngine.getRepositoryService().createDeployment()
                .addZipInputStream(new java.util.zip.ZipInputStream(bpmnFile.getInputStream(), Charset.forName("GBK")))
                .deploy();
        return true;
    }

    @HttpPost
    public List<?> getProcessDefinitionEntity() throws Throwable {
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

    public io.github.eeroom.entity.sfdb.bizdata startProcess(StartProcessParameter startProcessParameter) throws Throwable {
        //根据业务id获取对应流程的key
        var btp= this.dbContext.dbSet(biztype.class).select()
                .where(x->x.col(a->a.getid()).eq(startProcessParameter.getBizType()))
                .firstOrDefault();
        if(btp==null)
            throw new IllegalArgumentException("指定的业务类别不存在："+startProcessParameter.getBizType());
        var map=new HashMap<String,Object>();
        map.put("bizType",startProcessParameter.getBizType());
        map.put("createformdatajson",startProcessParameter.getCreateformdatajson());

        var processInstance = this.processEngine.getRuntimeService()
                .startProcessInstanceByKey(btp.getcamundaKey(),map);
        //往主业务表添加一条记录
        io.github.eeroom.entity.sfdb.bizdata biz=new io.github.eeroom.entity.sfdb.bizdata();
        biz.setbizType(startProcessParameter.getBizType());
        biz.setcreateBy(this.loginUserInfo.getName());
        biz.setcreateformdatajson(startProcessParameter.getCreateformdatajson());
        biz.setcreateTime(new java.sql.Timestamp(new Date().getTime()));
        biz.setprocessId(processInstance.getProcessInstanceId());
        this.dbContext.add(biz)
                .setInsertAllCol();
        this.dbContext.saveChange();
        return biz;
    }

    public List<?> getUserTask(){
        var lsttask= this.processEngine.getTaskService().createTaskQuery()
                .taskAssignee(this.loginUserInfo.getName())
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

    public  void  complete(CompleteTaskParameter completeTaskParameter) throws Throwable {
        //前置校验，当前登录人确实是这个task的处理人
        var task= this.processEngine.getTaskService().createTaskQuery().taskId(completeTaskParameter.getTaskId())
                .singleResult();
        if(task==null)
            throw new IllegalArgumentException("指定id的task不存在，taskid="+completeTaskParameter.getTaskId());
        if(!this.loginUserInfo.equals(task.getAssignee()))
            throw new IllegalArgumentException("你不是当前任务的处理人！");
        //更新主业务表子表，把状态置为close
        this.dbContext.edit(bizdatasub.class)
                .setUpdateCol(x->x.gettaskstatus(),1)
                .where(x->x.col(a->a.gettaskId()).eq(completeTaskParameter.getTaskId()))
                .where(x->x.col(a->a.gettaskstatus()).eq(0));
        this.dbContext.saveChange();
        //map中的键和可能值，流程图和completeTaskParameter要事先约定好，
        // 这里的约定是，UserTask完成后，如果需要根据完成的结果来决定后续不同的分支走向，就通过result这个参数，参数值的可能情况有：ok,deleget，驳回
        if(completeTaskParameter.getResult()== UserTaskResult.deleget){
            this.processEngine.getTaskService().delegateTask(completeTaskParameter.getTaskId(),completeTaskParameter.getDelegetHandler());
        }else {
            var map=new HashMap<String,Object>();
            map.put("result",completeTaskParameter.getResult());
            map.put("completeformdatajson",completeTaskParameter.getCompleteformdatajson());
            this.processEngine.getTaskService()
                    .complete(completeTaskParameter.getTaskId(),map);
        }
    }

}
