package io.github.eeroom.gtop.sf.camunda;

import io.github.eeroom.gtop.entity.sf.db.bizdata;
import io.github.eeroom.gtop.sf.MyDbContext;
import io.github.eeroom.gtop.sf.MyObjectFacotry;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class BizdataStatusSetter implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        var pid= delegateExecution.getProcessInstanceId();
        var dbcontext= MyObjectFacotry.getBean(MyDbContext.class);
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
}
