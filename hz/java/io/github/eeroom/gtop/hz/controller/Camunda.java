package io.github.eeroom.gtop.hz.controller;

import io.github.eeroom.gtop.entity.hz.StartProcessInput;
import io.github.eeroom.gtop.entity.camunda.CompleteTaskInput;
import io.github.eeroom.gtop.entity.hz.db.biztypeex;
import io.github.eeroom.gtop.entity.hz.db.bizdata;
import io.github.eeroom.gtop.hz.MyDbContext;
import io.github.eeroom.gtop.hz.aspnet.HttpPost;
import io.github.eeroom.gtop.hz.authen.CurrentUserInfo;
import io.github.eeroom.gtop.hz.authen.SkipAuthentication;
import io.github.eeroom.gtop.hz.camunda.ProcessInstanceHandler;
import io.github.eeroom.gtop.hz.serialize.JsonConvert;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class Camunda {
    org.camunda.bpm.engine.ProcessEngine processEngine;
    CurrentUserInfo currentUserInfo;
    MyDbContext dbContext;
    JsonConvert jsonConvert;
    ProcessInstanceHandler processInstanceHandler;
    public Camunda(org.camunda.bpm.engine.ProcessEngine processEngine, CurrentUserInfo loginUserInfo, MyDbContext dbContext,
                   JsonConvert jsonConvert, ProcessInstanceHandler processInstanceHandler){
        this.processEngine=processEngine;
        this.currentUserInfo =loginUserInfo;
        this.dbContext=dbContext;
        this.jsonConvert=jsonConvert;
        this.processInstanceHandler=processInstanceHandler;
    }


    @HttpPost
    @SkipAuthentication
    public boolean deploy(MultipartFile bpmXml, MultipartFile bpmPng) throws Throwable {
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
        var btpex= this.dbContext.dbSet(biztypeex.class).select()
                .where(x->x.col(a->a.getbizType()).eq(startProcessInput.getBizType()))
                .firstOrDefault();
        return this.processInstanceHandler.startProcess(btpex,startProcessInput.getFormdata());
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
        this.processInstanceHandler.complete(completeTaskInput.getTaskId(),completeTaskInput.getFormdata());
    }
}
