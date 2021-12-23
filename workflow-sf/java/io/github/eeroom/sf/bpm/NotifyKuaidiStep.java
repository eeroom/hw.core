package io.github.eeroom.sf.bpm;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.eeroom.entity.sfdb.bizdata;
import io.github.eeroom.sf.SfDbContext;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

import java.io.IOException;

public class NotifyKuaidiStep implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        var pid= delegateTask.getProcessInstanceId();
        var dbcontext=SfDbContext.newInstance();
        try {
            var bizd= dbcontext.dbSet(bizdata.class).select()
                    .where(x->x.col(a->a.getprocessId()).eq(pid))
                    .firstOrDefault();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        var completeformdatajson = (String)delegateTask.getVariable("completeformdatajson");
        com.fasterxml.jackson.databind.ObjectMapper objectMapper=new ObjectMapper();
        //反序列的时候，如果json中有额外的字段，class没有与其对应的属性，默认报错，这样配置就不会报错，
        //这个配置对于调用第三方接口特别重要，接口可能增加某个字段数据，但我们不需要用，也就没有修改代码
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            var fdata= objectMapper.readValue(completeformdatajson,io.github.eeroom.entity.KuaidiCheckResult.class);
            //调用第三方系统的回调接口，进行通知
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
