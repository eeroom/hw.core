package io.github.eeroom.sf.bpm;

import io.github.eeroom.entity.sfdb.bizdatasub;
import io.github.eeroom.nalu.Columns;
import io.github.eeroom.sf.MyObjectFacotry;
import io.github.eeroom.sf.SfDbContext;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AssigneeHook implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        var processId=delegateTask.getProcessInstanceId();
        var taskId=delegateTask.getId();
        var dbcontext= MyObjectFacotry.getBean(SfDbContext.class);
        var lstass= delegateTask.getAssignee().split(",");
        var lstbizdatasub= Arrays.stream(lstass).map(x->{
            var tmp=new bizdatasub();
            tmp.sethandlerId(x);
            tmp.setprocessId(processId);
            tmp.settaskId(taskId);
            tmp.setHandlerByMe(0);
            tmp.settaskstatus(0);
            return tmp;
        }).collect(Collectors.toList());
        dbcontext.add(lstbizdatasub)
                .setInsertCol(x-> Columns.of(x.gethandlerId(),x.getprocessId(),x.gettaskId(),x.getHandlerByMe(),x.gettaskstatus()));
        dbcontext.saveChange();
    }
}
