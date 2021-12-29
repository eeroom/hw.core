package io.github.eeroom.gtop.sf.camunda;

import io.github.eeroom.apiclient.HttpChannelFactory;
import io.github.eeroom.gtop.api.sf.kuaidi.IGuoneiKuaidiCallback;
import io.github.eeroom.gtop.entity.sf.db.kuaidientcustomer;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByCreate;
import io.github.eeroom.gtop.entity.sf.kuaidi.FeedMessage;
import io.github.eeroom.gtop.entity.sf.kuaidi.FeedType;
import io.github.eeroom.gtop.sf.MyDbContext;
import io.github.eeroom.gtop.sf.MyObjectFacotry;
import io.github.eeroom.gtop.sf.serialize.JsonConvert;
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
public class GuoneiKuaidiHandler extends ListenerHandler implements Serializable {

    public void feed(DelegateExecution delegateExecution,String feedtype,String... msgkeys) {
        var pid= delegateExecution.getProcessInstanceId();
        var dbcontext= MyObjectFacotry.getBean(MyDbContext.class);
        var formdataOfCreate=delegateExecution.getVariable(VariableKey.formdataOfCreate).toString();
        var enityByCreate= MyObjectFacotry.getBean(JsonConvert.class).deSerializeObject(formdataOfCreate, EntityByCreate.class);
        var customer= dbcontext.dbSet(kuaidientcustomer.class)
                .select()
                .where(x->x.col(a->a.getid()).eq(enityByCreate.getCustomerId()))
                .firstOrDefault();
        if(customer==null)
            throw new RuntimeException("指定的第三方不存在，info:"+enityByCreate.getCustomerId());
        //确定要发送的信息，流程图上指定
        HashMap<String,Object> map=new HashMap<>();
        for (var key:msgkeys){
            map.put(key,delegateExecution.getVariable(key));
        }
        var msg=new FeedMessage(pid, FeedType.valueOf(feedtype),map);
        var guoneiKuaidiCallback= HttpChannelFactory.createChannel(customer.getfeedbackurl(), IGuoneiKuaidiCallback.class);
        var rt= guoneiKuaidiCallback.waitmsg(msg);
    }
}
