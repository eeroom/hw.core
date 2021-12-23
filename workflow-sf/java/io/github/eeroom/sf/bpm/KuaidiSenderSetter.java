package io.github.eeroom.sf.bpm;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KuaidiSenderSetter implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        var createformdatajson = (String)delegateTask.getVariable("createformdatajson");
        com.fasterxml.jackson.databind.ObjectMapper objectMapper=new ObjectMapper();
        //反序列的时候，如果json中有额外的字段，class没有与其对应的属性，默认报错，这样配置就不会报错，
        //这个配置对于调用第三方接口特别重要，接口可能增加某个字段数据，但我们不需要用，也就没有修改代码
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            var fdata= objectMapper.readValue(createformdatajson,io.github.eeroom.entity.KuaidiCreateInput.class);
            delegateTask.setAssignee(fdata.getSender());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
