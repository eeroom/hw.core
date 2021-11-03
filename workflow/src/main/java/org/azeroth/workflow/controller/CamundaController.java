package org.azeroth.workflow.controller;

import org.azeroth.workflow.HttpPost;
import org.azeroth.workflow.viewModel.DeployAdd;
import org.camunda.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.camunda.bpm.engine.repository.Deployment;
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
    public DeployAdd add(DeployAdd deployAdd, MultipartFile bpmnFile) throws Throwable {
        var dlm= this.processEngine.getRepositoryService().createDeployment()
                .addZipInputStream(new java.util.zip.ZipInputStream(bpmnFile.getInputStream(), Charset.forName("GBK")))
                .deploy();
        return new DeployAdd();
    }

    @HttpPost
    public List<?> getAllDeploy() throws Throwable {
        var lstDeployment= this.processEngine.getRepositoryService().createDeploymentQuery()
                .orderByDeploymentId()
                .asc()
                .list()
                .stream()
                .map(x->new Deployment() {
                    @Override
                    public String getId() {
                        return x.getId();
                    }

                    @Override
                    public String getName() {
                        return x.getName();
                    }

                    @Override
                    public Date getDeploymentTime() {
                        return x.getDeploymentTime();
                    }

                    @Override
                    public String getSource() {
                        return x.getSource();
                    }

                    @Override
                    public String getTenantId() {
                        return x.getTenantId();
                    }
                })
                .collect(java.util.stream.Collectors.toList());
        return lstDeployment;
    }
}
