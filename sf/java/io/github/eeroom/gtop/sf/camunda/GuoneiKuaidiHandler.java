package io.github.eeroom.gtop.sf.camunda;

import io.github.eeroom.apiclient.HttpChannelFactory;
import io.github.eeroom.gtop.api.sf.kuaidi.IGuoneiKuaidiCallback;
import io.github.eeroom.gtop.entity.sf.db.kuaidientcustomer;
import io.github.eeroom.gtop.entity.sf.kuaidi.FeedMessage;
import io.github.eeroom.gtop.entity.sf.kuaidi.FeedType;
import io.github.eeroom.gtop.sf.MyDbContext;
import io.github.eeroom.gtop.sf.MyObjectFacotry;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 可以取代KuaidiFeeder
 * bean的名称约定：流程图的key+Handler,
 * 弊端：流程图的key和这个bean名称强绑定
 * 问题不大
 */
@Component
@Qualifier("GuoneiKuaidiHandler")
public class GuoneiKuaidiHandler implements Serializable {

    public void feed(DelegateExecution delegateExecution) {
        var pid= delegateExecution.getProcessInstanceId();
        var dbcontext= MyObjectFacotry.getBean(MyDbContext.class);
        var thirdpartId=delegateExecution.getVariable("thirdpartId").toString();
        var thridpart= dbcontext.dbSet(kuaidientcustomer.class)
                .select()
                .where(x->x.col(a->a.getid()).eq(thirdpartId))
                .firstOrDefault();
        if(thridpart==null)
            throw new RuntimeException("指定的第三方不存在，info:"+thirdpartId);
        //确定要发送的信息，流程图上指定
        var msgkeys= (String)delegateExecution.getVariable("msgkeys");
        var lstmsgkey=msgkeys.split(",");
        HashMap<String,Object> data=new HashMap<>();
        for (var key:lstmsgkey){
            data.put(key,delegateExecution.getVariable(key));
        }
        var msg=new FeedMessage(pid, FeedType.valueOf(delegateExecution.getVariable("msgtype").toString()),data);
        var thirdpartCallbackHandler= HttpChannelFactory.createChannel(thridpart.getfeedbackurl(), IGuoneiKuaidiCallback.class);
        var rt= thirdpartCallbackHandler.waitmsg(msg);
    }
}
