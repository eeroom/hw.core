package io.github.eeroom.gtop.hz.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.eeroom.gtop.entity.camunda.ApproveType;
import io.github.eeroom.gtop.entity.camunda.CompleteType;
import io.github.eeroom.gtop.entity.camunda.StartProcessInput;
import io.github.eeroom.gtop.entity.hz.ApiAlias;
import io.github.eeroom.gtop.entity.hz.db.bizapicfg;
import io.github.eeroom.gtop.entity.hz.db.bizdata;
import io.github.eeroom.gtop.entity.hz.kuaidi.ApproveResult;
import io.github.eeroom.gtop.entity.hz.kuaidi.EntityByCreate;
import io.github.eeroom.gtop.hz.MyDbContext;
import io.github.eeroom.gtop.hz.serialize.JsonConvert;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class KuaidiController {
    MyDbContext myDbContext;
    CamundaController camundaController;
    JsonConvert jsonConvert;
    public KuaidiController(MyDbContext myDbContext, CamundaController camundaController, JsonConvert jsonConvert){
        this.camundaController=camundaController;
        this.jsonConvert=jsonConvert;
        this.myDbContext=myDbContext;
    }
    public bizdata create(EntityByCreate entityByCreate){
        //参数校验

        var cfg=this.myDbContext.dbSet(bizapicfg.class).select()
                .where(x->x.col(a->a.getapi()).eq(ApiAlias.快递审批))
                .firstOrDefault();
        if(cfg==null)
            throw new RuntimeException(String.format("找不到bziapicfg数据，请联系管理员配置。api:%s",ApiAlias.快递审批.name()));
        var pa=new StartProcessInput();
        pa.setProcdefKey(cfg.getprocdefKey());
        var map=this.jsonConvert.deSerializeObject(this.jsonConvert.serializeObject(entityByCreate), new TypeReference<HashMap<String,Object>>() {});
        pa.setFormdata(map);
        return this.camundaController.startProcess(pa);
    }

    public Boolean approve(ApproveResult approveResult){
        //参数校验
        var cinput=new io.github.eeroom.gtop.entity.camunda.CompleteTaskInput();
        cinput.setTaskId(approveResult.getTaskId());
        if(approveResult.getApproveType().equals(ApproveType.转审)){
            cinput.setCompleteType(CompleteType.delegate);
            cinput.setDelegateAssignee(approveResult.getNewHandler());
        }
        var map=this.jsonConvert.deSerializeObject(this.jsonConvert.serializeObject(approveResult), new TypeReference<HashMap<String,Object>>() {});
        cinput.setFormdata(map);
        this.camundaController.complete(cinput);
        return  true;
    }
}
