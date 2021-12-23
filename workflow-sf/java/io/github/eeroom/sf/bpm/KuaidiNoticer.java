package io.github.eeroom.sf.bpm;

import io.github.eeroom.apiclient.HttpChannelFactory;
import io.github.eeroom.entity.sf.kuaidi.INoticer;
import io.github.eeroom.entity.sf.kuaidi.NoticeMessage;
import io.github.eeroom.entity.sf.kuaidi.EntityByCreate;
import io.github.eeroom.entity.sfdb.bizdata;
import io.github.eeroom.sf.JsonHelper;
import io.github.eeroom.sf.MyObjectFacotry;
import io.github.eeroom.sf.SfDbContext;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

import java.util.Arrays;
import java.util.HashMap;

public class KuaidiNoticer implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        var pid= delegateTask.getProcessInstanceId();
        var dbcontext= MyObjectFacotry.getBean(SfDbContext.class);
        var thirdpartId=delegateTask.getVariable("thirdpartId").toString();
        var thridpart= dbcontext.dbSet(io.github.eeroom.entity.sfdb.jijiancustomer.class)
                .select()
                .where(x->x.col(a->a.getid()).eq(thirdpartId))
                .firstOrDefault();
        if(thridpart==null)
            throw new RuntimeException("指定的第三方不存在，info:"+thirdpartId);
        var iKuaidiNotice= HttpChannelFactory.createChannel(thridpart.getcallbackurl(), INoticer.class);
        //确定要发送的信息，流程图上指定
        var msgkeys= (String)delegateTask.getVariable("msgkeys");
        var lstmsgkey=msgkeys.split(",");
        HashMap<String,Object> data=new HashMap<>();
        for (var key:lstmsgkey){
            data.put(key,delegateTask.getVariable(key));
        }
        var msg=new NoticeMessage(pid,(String)delegateTask.getVariable("msgtype"),data);
        var rt= iKuaidiNotice.send(msg);
    }
}
