package io.github.eeroom.gtop.sf.camunda;

import io.github.eeroom.gtop.entity.sf.db.bizdatasub;
import io.github.eeroom.gtop.sf.MyObjectFacotry;
import io.github.eeroom.gtop.sf.MyDbContext;
import io.github.eeroom.nalu.Columns;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AssigneeHook implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        var processId=delegateTask.getProcessInstanceId();
        var taskId=delegateTask.getId();
        var dbcontext= MyObjectFacotry.getBean(MyDbContext.class);
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
