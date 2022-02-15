package io.github.eeroom.workflow.controller;

import io.github.eeroom.workflow.HttpPost;
import io.github.eeroom.workflow.viewModel.DeployAdd;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class Deploy {


    ProcessEngine processEngine;

    //@PostMapping(path = "Deploy/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @HttpPost
    public DeployAdd add(DeployAdd deployAdd, MultipartFile bpmnFile){
        return new DeployAdd();
    }
}
