package io.github.eeroom.sf.bpm;

import io.github.eeroom.entity.sfdb.bizdatasub;
import io.github.eeroom.nalu.Columns;
import io.github.eeroom.sf.SfDbContext;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class AssigneeHook implements TaskListener {
    SfDbContext dbContext;
    public AssigneeHook(SfDbContext dbContext){
        this.dbContext=dbContext;
    }
    @Override
    public void notify(DelegateTask delegateTask) {
        var processId=delegateTask.getProcessInstanceId();
        var taskId=delegateTask.getId();

        var lstass= delegateTask.getAssignee().split(",");
        var lstbizdatasub= Arrays.stream(lstass).map(x->{
            var tmp=new bizdatasub();
            tmp.sethandlerId(x);
            tmp.setprocessId(processId);
            tmp.settaskId(taskId);
            tmp.setresult(0);
            tmp.settaskstatus(0);
            return tmp;
        }).collect(Collectors.toList());
        try {
            this.dbContext.add(lstbizdatasub)
                    .setInsertCol(x-> Columns.of(x.gethandlerId(),x.getprocessId(),x.gettaskId(),x.getresult(),x.gettaskstatus()));
            this.dbContext.saveChange();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
