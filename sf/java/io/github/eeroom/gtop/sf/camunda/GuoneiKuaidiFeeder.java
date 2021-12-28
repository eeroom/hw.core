package io.github.eeroom.gtop.sf.camunda;

import io.github.eeroom.apiclient.HttpChannelFactory;
import io.github.eeroom.gtop.entity.sf.db.kuaidientcustomer;
import io.github.eeroom.gtop.api.sf.kuaidi.IGuoneiKuaidiCallback;
import io.github.eeroom.gtop.entity.sf.kuaidi.FeedMessage;
import io.github.eeroom.gtop.entity.sf.kuaidi.FeedType;
import io.github.eeroom.gtop.sf.MyObjectFacotry;
import io.github.eeroom.gtop.sf.MyDbContext;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

import java.util.HashMap;

/**
 * 被KuaidiHandler取代
 */
public class GuoneiKuaidiFeeder implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        var pid= delegateExecution.getProcessInstanceId();
        var dbcontext= MyObjectFacotry.getBean(MyDbContext.class);
        var thirdpartId=delegateExecution.getVariable("thirdpartId").toString();
        var thridpart= dbcontext.dbSet(kuaidientcustomer.class)
                .select()
                .where(x->x.col(a->a.getid()).eq(thirdpartId))
                .firstOrDefault();
        if(thridpart==null)
            throw new RuntimeException("指定的第三方不存在，info:"+thirdpartId);
        var iKuaidiNotice= HttpChannelFactory.createChannel(thridpart.getfeedbackurl(), IGuoneiKuaidiCallback.class);
        //确定要发送的信息，流程图上指定
        var msgkeys= (String)delegateExecution.getVariable("msgkeys");
        var lstmsgkey=msgkeys.split(",");
        HashMap<String,Object> data=new HashMap<>();
        for (var key:lstmsgkey){
            data.put(key,delegateExecution.getVariable(key));
        }
        var msg=new FeedMessage(pid, FeedType.valueOf(delegateExecution.getVariable("msgtype").toString()),data);
        var rt= iKuaidiNotice.waitmsg(msg);
    }
}
