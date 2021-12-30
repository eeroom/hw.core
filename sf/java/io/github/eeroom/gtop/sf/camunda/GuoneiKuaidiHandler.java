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
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 约定：往各个流程实例注入handler的时候，
 * 如果存在命名为[流程图key+Handler]的Handler类型，那么listenerHandler就会注入[流程图key+Handler]类型的实例
 * 否则，直接注入ListenerHandler的实例
 * 所定义的Handler需要标注为@Component注册到springcontext容器
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
        var msg=new FeedMessage();
        msg.setData(map);
        msg.setProcessInstanceId(pid);
        msg.setType(FeedType.valueOf(feedtype));
        var guoneiKuaidiCallback= HttpChannelFactory.createChannel(customer.getfeedbackurl(), IGuoneiKuaidiCallback.class);
        var rt= guoneiKuaidiCallback.execute(msg);
    }
}
