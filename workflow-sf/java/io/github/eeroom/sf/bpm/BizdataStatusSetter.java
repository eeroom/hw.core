package io.github.eeroom.sf.bpm;

import io.github.eeroom.entity.sfdb.bizdata;
import io.github.eeroom.sf.MyObjectFacotry;
import io.github.eeroom.sf.SfDbContext;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class BizdataStatusSetter implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        var pid= delegateTask.getProcessInstanceId();
        var dbcontext= MyObjectFacotry.getBean(SfDbContext.class);
        var bizd= dbcontext.dbSet(bizdata.class).select()
                .where(x->x.col(a->a.getprocessId()).eq(pid))
                .firstOrDefault();
        if(bizd==null)
            throw new RuntimeException("没有找到对应的bizdata,processId:"+pid);
        var bizdatastatus=delegateTask.getVariable("bizdatastatus");
        var value= Integer.valueOf(bizdatastatus.toString());
        dbcontext.edit(bizdata.class)
                .setUpdateCol(x->x.getStatus(),value)
                .where(x->x.col(a->a.getprocessId()).eq(bizd.getprocessId()));
        dbcontext.saveChange();
    }
}
