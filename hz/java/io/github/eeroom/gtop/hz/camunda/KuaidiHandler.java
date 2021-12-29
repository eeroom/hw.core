package io.github.eeroom.gtop.hz.camunda;

import io.github.eeroom.gtop.entity.hz.db.bizdataex;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByPaymoney;
import io.github.eeroom.gtop.api.sf.kuaidi.IGuoneiKuaidiController;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByCreate;
import io.github.eeroom.gtop.hz.AppConfig;
import io.github.eeroom.gtop.hz.MyDbContext;
import io.github.eeroom.gtop.hz.MyObjectFacotry;
import io.github.eeroom.gtop.hz.serialize.JsonConvert;
import io.github.eeroom.nalu.Columns;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class KuaidiHandler  implements Serializable {
        public void newTransfer(DelegateExecution delegateExecution){
                //读取表单数据，组织sf那边需要的参数格式，调用sf的接口
                var formdatastr= delegateExecution.getVariable(VariableKey.formdataOfCreate).toString();
                var formdata= MyObjectFacotry.getBean(JsonConvert.class).deSerializeObject(formdatastr, EntityByCreate.class);
                var config=MyObjectFacotry.getBean(AppConfig.class);
                formdata.setCustomerId(config.kuaidimycode);
                var sfkuaidiHandler= io.github.eeroom.apiclient.HttpChannelFactory.createChannel(config.kuaidiSfUrl, IGuoneiKuaidiController.class);
                var rt= sfkuaidiHandler.create(formdata);
                //保存数据,
                // 这里不进行savechange,由统一的添加bizdata的地方进行savechange
                var bizdex=new bizdataex();
                bizdex.setprocessId(delegateExecution.getProcessInstanceId());
                bizdex.seteKey(VariableKey.processInstanceIdBySf);
                bizdex.seteValue(rt.getprocessId());
                var dbcontext=MyObjectFacotry.getBean(MyDbContext.class);
                dbcontext.add(bizdex).setInsertCol(x-> Columns.of(x.geteKey(),x.geteValue(),x.getprocessId()));
        }

        public void completePaymoney(DelegateExecution delegateExecution){
                var dbcontext=MyObjectFacotry.getBean(MyDbContext.class);
                var lstbizdataex= dbcontext.dbSet(bizdataex.class).select()
                        .where(x->x.col(a->a.getprocessId()).eq(delegateExecution.getProcessInstanceId()))
                        .where(x->x.col(a->a.geteKey()).eq(VariableKey.processInstanceIdBySf))
                        .toList();
                if(lstbizdataex.size()<1)
                        throw new RuntimeException(String.format("没有找到该流程在sf系统对应的快递流程id,本流程id：%s",delegateExecution.getProcessInstanceId()));
                if (lstbizdataex.size()>1)
                        throw new RuntimeException(String.format("该流程在sf系统对应的快递流程id存在多个,本流程id：%s,sf流程id:%s",
                                delegateExecution.getProcessInstanceId(),
                                MyObjectFacotry.getBean(JsonConvert.class).serializeObject(lstbizdataex)));
                var config=MyObjectFacotry.getBean(AppConfig.class);
                var formdatastr= delegateExecution.getVariable(VariableKey.formdataOfComplete).toString();
                var payentity= MyObjectFacotry.getBean(JsonConvert.class).deSerializeObject(formdatastr,EntityByPaymoney.class);
                payentity.setProcessInstanceId(lstbizdataex.get(0).geteValue());//这个值非常关键，hz系统的表单提交数据不涉及sf系统的流程实例id
                payentity.setThirdpartId(config.kuaidimycode);
                var sfkuaidiHandler= io.github.eeroom.apiclient.HttpChannelFactory.createChannel(config.kuaidiSfUrl, IGuoneiKuaidiController.class);
                sfkuaidiHandler.paymoney(payentity);
        }
}
