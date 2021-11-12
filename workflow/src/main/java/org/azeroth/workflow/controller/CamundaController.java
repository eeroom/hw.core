package org.azeroth.workflow.controller;

import org.azeroth.workflow.HttpPost;
import org.azeroth.workflow.viewModel.DeployAdd;
import org.azeroth.workflow.viewModel.StartProcessParameter;
import org.camunda.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CamundaController {

    @Autowired
    org.camunda.bpm.engine.ProcessEngine processEngine;

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


}
