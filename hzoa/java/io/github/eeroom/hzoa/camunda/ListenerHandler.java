package io.github.eeroom.hzoa.camunda;

import io.github.eeroom.hzcore.BizDataStatus;
import io.github.eeroom.hzcore.TaskStatus;
import io.github.eeroom.hzcore.hzoa.db.bizdatasub;
import io.github.eeroom.hzoa.MyDbContext;
import io.github.eeroom.hzoa.MyObjectFacotry;
import io.github.eeroom.nalu.Columns;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class ListenerHandler implements Serializable {
    /**
     * 可以取代AssigneeHook
     * @param delegateTask
     */
    public void syncAssigneeToBizdatasub(DelegateTask delegateTask) {
        var processId=delegateTask.getProcessInstanceId();
        var taskId=delegateTask.getId();
        var dbcontext= MyObjectFacotry.getBean(MyDbContext.class);
        var lstass= delegateTask.getAssignee().split(",");
        var lstbizdatasub= Arrays.stream(lstass).map(x->{
            var tmp=new bizdatasub();
            tmp.setassignee(x);
            tmp.setprocessId(processId);
            tmp.settaskId(taskId);
            tmp.setassigneeCompleted(TaskStatus.处理中);
            tmp.setstatus(TaskStatus.处理中);
            return tmp;
        }).collect(Collectors.toList());
        dbcontext.add(lstbizdatasub)
                .setInsertCol(x-> Columns.of(x.getassignee(),x.getprocessId(),x.gettaskId(),x.getassigneeCompleted(),x.getstatus()));
    }

    /**
     * 可以取代BizdataStatusSetter
     * @param delegateTask
     */
    public void updateBizdataStatusByTask(DelegateTask delegateTask,String status) {
        var pid= delegateTask.getProcessInstanceId();
        this.updateBizdataStatus(pid,status);
    }

    private void updateBizdataStatus(String pid, String status) {
        var dbcontext= MyObjectFacotry.getBean(MyDbContext.class);
        var bizd= dbcontext.dbSet(io.github.eeroom.hzcore.hzkd.db.bizdata.class).select()
                .where(x->x.col(a->a.getprocessId()).eq(pid))
                .firstOrDefault();
        if(bizd==null)
            throw new RuntimeException("没有找到对应的bizdata,processId:"+pid);
        var value= BizDataStatus.valueOf(status);
        dbcontext.edit(io.github.eeroom.hzcore.hzkd.db.bizdata.class)
                .setUpdateCol(x->x.getstatus(),value)
                .where(x->x.col(a->a.getprocessId()).eq(bizd.getprocessId()));
    }

    public void updateBizdataStatusByExecution(DelegateExecution delegateExecution, String status) {
        var pid= delegateExecution.getProcessInstanceId();
        this.updateBizdataStatus(pid,status);
    }

    public String concat(String... value){
        return String.join("",value);
    }
}
