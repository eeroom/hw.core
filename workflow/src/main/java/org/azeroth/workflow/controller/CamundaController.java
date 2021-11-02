package org.azeroth.workflow.controller;

import org.azeroth.workflow.HttpPost;
import org.azeroth.workflow.viewModel.DeployAdd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CamundaController {

    @Autowired
    org.camunda.bpm.engine.ProcessEngine processEngine;

    @HttpPost
    public DeployAdd add(DeployAdd deployAdd, MultipartFile bpmnFile) throws Throwable {

        var dlm= this.processEngine.getRepositoryService().createDeployment()
                .addZipInputStream(new java.util.zip.ZipInputStream(bpmnFile.getInputStream()))
                .deploy();
        return new DeployAdd();
    }
}
