package io.github.eeroom.gtop.hz.camunda;

import io.github.eeroom.gtop.entity.hz.db.bizdataex;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByPaymoney;
import io.github.eeroom.gtop.api.sf.kuaidi.IGuoneiKuaidiController;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByCreate;
import io.github.eeroom.gtop.hz.AppConfig;
import io.github.eeroom.gtop.hz.MyDbContext;
import io.github.eeroom.gtop.hz.MyObjectFacotry;
import io.github.eeroom.gtop.hz.authen.CurrentUserInfo;
import io.github.eeroom.gtop.hz.serialize.JsonConvert;
import io.github.eeroom.nalu.Columns;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class KuaidiHandler extends ListenerHandler  implements Serializable {
        public void createKuaidi(DelegateExecution delegateExecution){
                //读取表单数据，组织sf那边需要的参数格式，调用sf的接口
                var formdatastr= delegateExecution.getVariable(VariableKey.formdataOfCreate).toString();
                var entityByCreate= MyObjectFacotry.getBean(JsonConvert.class).deSerializeObject(formdatastr, EntityByCreate.class);
                var config=MyObjectFacotry.getBean(AppConfig.class);
                entityByCreate.setCustomerId(config.kuaidimycode);
                //默认已当前登录人作为发送人
                entityByCreate.setSender(MyObjectFacotry.getBean(CurrentUserInfo.class).getAccount());
                var sfkuaidiHandler= io.github.eeroom.apiclient.HttpChannelFactory.createChannel(config.kuaidiSfUrl, IGuoneiKuaidiController.class);
                var rt= sfkuaidiHandler.create(entityByCreate);
                //保存数据,
                // 这里不进行savechange,由统一的添加bizdata的地方进行savechange
                var bizdex=new bizdataex();
                bizdex.setprocessId(delegateExecution.getProcessInstanceId());
                bizdex.seteKey(VariableKey.processInstanceIdOfSf);
                bizdex.seteValue(rt.getprocessId());
                var dbcontext=MyObjectFacotry.getBean(MyDbContext.class);
                dbcontext.add(bizdex).setInsertCol(x-> Columns.of(x.geteKey(),x.geteValue(),x.getprocessId()));
                delegateExecution.setVariable(VariableKey.sf,VariableKey.sf);
        }

        public void payKuaidi(DelegateExecution delegateExecution,Integer flag){
                var dbcontext=MyObjectFacotry.getBean(MyDbContext.class);
                var bizdataex= dbcontext.dbSet(bizdataex.class).select()
                        .where(x->x.col(a->a.getprocessId()).eq(delegateExecution.getProcessInstanceId()))
                        .where(x->x.col(a->a.geteKey()).eq(VariableKey.processInstanceIdOfSf))
                        .firstOrDefault();
                if(bizdataex==null)
                        throw new RuntimeException(String.format("没有找到该流程在sf系统对应的快递流程id,本流程id：%s",delegateExecution.getProcessInstanceId()));
                var appConfig=MyObjectFacotry.getBean(AppConfig.class);
                var formdatastr= delegateExecution.getVariable(VariableKey.formdataOfComplete).toString();
                var payentity= MyObjectFacotry.getBean(JsonConvert.class).deSerializeObject(formdatastr,EntityByPaymoney.class);
                payentity.setProcessInstanceId(bizdataex.geteValue());//这个值非常关键，hz系统的表单提交数据不涉及sf系统的流程实例id
                payentity.setCustomerId(appConfig.kuaidimycode);
                var sfkuaidiHandler= io.github.eeroom.apiclient.HttpChannelFactory.createChannel(appConfig.kuaidiSfUrl, IGuoneiKuaidiController.class);
                sfkuaidiHandler.paymoney(payentity);
        }
}
