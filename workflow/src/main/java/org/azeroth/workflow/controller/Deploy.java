package org.azeroth.workflow.controller;

import org.azeroth.workflow.HttpPost;
import org.azeroth.workflow.viewModel.DeployAdd;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class Deploy {


    ProcessEngine processEngine;

    //这里涉及文件和普通表单数据，asp.net风格的改动和swaage-ui配合的不好，所以使用PostMapping
    //使用asp.net风格的做法，这里仍然ok,但是swagger-ui不能正常工作，
    @PostMapping(path = "Deploy/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeployAdd add(DeployAdd deployAdd, MultipartFile bpmnFile){
        return new DeployAdd();
    }
}
