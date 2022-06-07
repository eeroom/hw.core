package io.github.eeroom.hzkd.camunda;

import io.github.eeroom.remoting.HttpChannelFactory;
import io.github.eeroom.hzcore.hzkd.api.IGuoneiKuaidiCallback;
import io.github.eeroom.hzcore.hzkd.db.kuaidientcustomer;
import io.github.eeroom.hzcore.hzkd.guonei.EntityByCreate;
import io.github.eeroom.hzcore.hzkd.guonei.FeedMessage;
import io.github.eeroom.hzcore.hzkd.guonei.FeedType;
import io.github.eeroom.hzkd.MyDbContext;
import io.github.eeroom.hzkd.MyObjectFacotry;
import io.github.eeroom.hzkd.serialize.JsonConvert;
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
