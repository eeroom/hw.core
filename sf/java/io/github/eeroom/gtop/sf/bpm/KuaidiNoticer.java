package io.github.eeroom.gtop.sf.bpm;

import io.github.eeroom.apiclient.HttpChannelFactory;
import io.github.eeroom.gtop.entity.sf.db.jijiancustomer;
import io.github.eeroom.gtop.api.sf.INoticer;
import io.github.eeroom.gtop.entity.sf.kuaidi.NoticeMessage;
import io.github.eeroom.gtop.entity.sf.kuaidi.NoticeMessageType;
import io.github.eeroom.gtop.sf.MyObjectFacotry;
import io.github.eeroom.gtop.sf.SfDbContext;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

import java.util.HashMap;

public class KuaidiNoticer implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        var pid= delegateExecution.getProcessInstanceId();
        var dbcontext= MyObjectFacotry.getBean(SfDbContext.class);
        var thirdpartId=delegateExecution.getVariable("thirdpartId").toString();
        var thridpart= dbcontext.dbSet(jijiancustomer.class)
                .select()
                .where(x->x.col(a->a.getid()).eq(thirdpartId))
                .firstOrDefault();
        if(thridpart==null)
            throw new RuntimeException("指定的第三方不存在，info:"+thirdpartId);
        var iKuaidiNotice= HttpChannelFactory.createChannel(thridpart.getcallbackurl(), INoticer.class);
        //确定要发送的信息，流程图上指定
        var msgkeys= (String)delegateExecution.getVariable("msgkeys");
        var lstmsgkey=msgkeys.split(",");
        HashMap<String,Object> data=new HashMap<>();
        for (var key:lstmsgkey){
            data.put(key,delegateExecution.getVariable(key));
        }
        var msg=new NoticeMessage(pid, NoticeMessageType.valueOf(delegateExecution.getVariable("msgtype").toString()),data);
        var rt= iKuaidiNotice.send(msg);
    }
}
