package org.azeroth.workflow.controller;

import org.azeroth.workflow.HttpPost;
import org.azeroth.workflow.LoginUserInfo;
import org.azeroth.workflow.viewModel.DeployAdd;
import org.azeroth.workflow.viewModel.CompleteTaskParameter;
import org.azeroth.workflow.viewModel.StartProcessParameter;
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

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CamundaController {

    @Autowired
    org.camunda.bpm.engine.ProcessEngine processEngine;

    @Autowired
    LoginUserInfo loginUserInfo;

    @HttpPost
    public DeployAdd deploy(DeployAdd deployAdd, MultipartFile bpmnFile) throws Throwable {
        var dlm = this.processEngine.getRepositoryService().createDeployment()
                .addZipInputStream(new java.util.zip.ZipInputStream(bpmnFile.getInputStream(), Charset.forName("GBK")))
                .deploy();
        return new DeployAdd();
    }

    @HttpPost
    public List<?> getProcessDefinitionEntity() throws Throwable {
        var lstDeployment = this.processEngine.getRepositoryService().createProcessDefinitionQuery()
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
        return lstDeployment;
    }

    public ProcessInstance startProcess(StartProcessParameter startProcessParameter) {

        var x = this.processEngine.getRuntimeService()
                .startProcessInstanceByKey(startProcessParameter.getKey());
        var tmp=new ProcessInstance(){
            @Override
            public String getProcessDefinitionId() {
                return x.getProcessDefinitionId();
            }

            @Override
            public String getBusinessKey() {
                return x.getBusinessKey();
            }

            @Override
            public String getCaseInstanceId() {
                return x.getCaseInstanceId();
            }

            @Override
            public String getId() {
                return x.getId();
            }

            @Override
            public boolean isSuspended() {
                return x.isSuspended();
            }

            @Override
            public boolean isEnded() {
                return x.isEnded();
            }

            @Override
            public String getProcessInstanceId() {
                return x.getProcessInstanceId();
            }

            @Override
            public String getTenantId() {
                return x.getTenantId();
            }
        };
        return tmp;
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

    public  void  complete(CompleteTaskParameter completeTaskParameter){
        //map中的键和可能值，流程图和completeTaskParameter要事先约定好，
        // 这里的约定是，UserTask完成后，如果需要根据完成的结果来决定后续不同的分支走向，就通过result这个参数，参数值的可能情况有：ok,deleget，驳回
        if(completeTaskParameter.getResult().equals("deleget")){
            //转审批
            this.processEngine.getTaskService().delegateTask(completeTaskParameter.getId(),completeTaskParameter.getTag());
            return;
        }
        var map=new HashMap<String,Object>();
        map.put("result",completeTaskParameter.getResult());
        this.processEngine.getTaskService()
                .complete(completeTaskParameter.getId(),map);
    }

}
