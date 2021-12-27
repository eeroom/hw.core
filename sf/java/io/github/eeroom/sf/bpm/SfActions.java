package io.github.eeroom.sf.bpm;

import io.github.eeroom.entity.sf.db.bizdata;
import io.github.eeroom.entity.sf.db.bizdatasub;
import io.github.eeroom.nalu.Columns;
import io.github.eeroom.sf.MyObjectFacotry;
import io.github.eeroom.sf.SfDbContext;
import org.camunda.bpm.engine.delegate.DelegateTask;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SfActions implements Serializable {
    static final long serialVersionUID = 42L;
    /**
     * 可以取代AssigneeHook
     * @param delegateTask
     */
    public void syncAssigneeToBizdatasub(DelegateTask delegateTask) {
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

    /**
     * 可以取代BizdataTitleSetter
     * @param delegateExecution
     */
    public void updateBizdataTitle(DelegateTask delegateExecution) {
        var pid= delegateExecution.getProcessInstanceId();
        var dbcontext= MyObjectFacotry.getBean(SfDbContext.class);
        var bizd= dbcontext.dbSet(bizdata.class).select()
                .where(x->x.col(a->a.getprocessId()).eq(pid))
                .firstOrDefault();
        if(bizd==null)
            throw new RuntimeException("没有找到对应的bizdata,processId:"+pid);
        var title=delegateExecution.getVariable("title");
        dbcontext.edit(bizdata.class)
                .setUpdateCol(x->x.gettitle(),title)
                .where(x->x.col(a->a.getprocessId()).eq(bizd.getprocessId()));
        dbcontext.saveChange();
    }

    /**
     * 可以取代BizdataStatusSetter
     * @param delegateExecution
     */
    public void updateBizdataStatus(DelegateTask delegateExecution) {
        var pid= delegateExecution.getProcessInstanceId();
        var dbcontext= MyObjectFacotry.getBean(SfDbContext.class);
        var bizd= dbcontext.dbSet(bizdata.class).select()
                .where(x->x.col(a->a.getprocessId()).eq(pid))
                .firstOrDefault();
        if(bizd==null)
            throw new RuntimeException("没有找到对应的bizdata,processId:"+pid);
        var bizdatastatus=delegateExecution.getVariable("bizdatastatus");
        var value= Integer.valueOf(bizdatastatus.toString());
        dbcontext.edit(bizdata.class)
                .setUpdateCol(x->x.getStatus(),value)
                .where(x->x.col(a->a.getprocessId()).eq(bizd.getprocessId()));
        dbcontext.saveChange();
    }

    public String concat(String... value){
        return String.join("",value);
    }
}
