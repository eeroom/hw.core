package io.github.eeroom.hz.camunda;

import io.github.eeroom.entity.hzdb.bizdataex;
import io.github.eeroom.entity.sf.kuaidi.EntityByPaymoney;
import io.github.eeroom.entity.sf.kuaidi.IKuaidiController;
import io.github.eeroom.hz.ApplicationConfig;
import io.github.eeroom.hz.MyDbContext;
import io.github.eeroom.hz.MyObjectFacotry;
import io.github.eeroom.hz.serialize.JsonConvert;
import io.github.eeroom.nalu.Columns;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class KuaidiHandler  implements Serializable {
        public void newTransfer(DelegateExecution delegateExecution){
                //读取表单数据，组织sf那边需要的参数格式，调用sf的接口
                var formdatastr= delegateExecution.getVariable(MapKeys.formdataOfCreate).toString();
                var formdata= MyObjectFacotry.getBean(JsonConvert.class).deSerializeObject(formdatastr,io.github.eeroom.entity.sf.kuaidi.EntityByCreate.class);
                var config=MyObjectFacotry.getBean(ApplicationConfig.class);
                formdata.setThirdpartId(config.kuaidimycode);
                var kc= io.github.eeroom.apiclient.HttpChannelFactory.createChannel(config.kuaidiSfUrl, IKuaidiController.class);
                var rt= kc.newTransfer(formdata);
                //保存数据,
                // 这里不进行savechange,由统一的添加bizdata的地方进行savechange
                var bizdex=new bizdataex();
                bizdex.setprocessId(delegateExecution.getProcessInstanceId());
                bizdex.seteKey(MapKeys.processInstanceIdBySf);
                bizdex.seteValue(rt.getprocessId());
                var dbcontext=MyObjectFacotry.getBean(MyDbContext.class);
                dbcontext.add(bizdex).setInsertCol(x-> Columns.of(x.geteKey(),x.geteValue(),x.getprocessId()));
        }

        public void completePaymoney(DelegateExecution delegateExecution){
                var dbcontext=MyObjectFacotry.getBean(MyDbContext.class);
                var lstex= dbcontext.dbSet(bizdataex.class).select()
                        .where(x->x.col(a->a.getprocessId()).eq(delegateExecution.getProcessInstanceId()))
                        .where(x->x.col(a->a.geteKey()).eq(MapKeys.processInstanceIdBySf))
                        .toList();
                if(lstex.size()<1)
                        throw new RuntimeException(String.format("没有找到该流程在sf系统对应的快递流程id,本流程id：%s",delegateExecution.getProcessInstanceId()));
                if (lstex.size()>1)
                        throw new RuntimeException(String.format("该流程在sf系统对应的快递流程id存在多个,本流程id：%s,sf流程id:%s",
                                delegateExecution.getProcessInstanceId(),
                                MyObjectFacotry.getBean(JsonConvert.class).serializeObject(lstex)));
                var config=MyObjectFacotry.getBean(ApplicationConfig.class);
                var kc= io.github.eeroom.apiclient.HttpChannelFactory.createChannel(config.kuaidiSfUrl, IKuaidiController.class);
                var formdatastr= delegateExecution.getVariable(MapKeys.formdataOfComplete).toString();
                var payentity= MyObjectFacotry.getBean(JsonConvert.class).deSerializeObject(formdatastr,EntityByPaymoney.class);
                payentity.setProcessInstanceId(lstex.get(0).geteValue());//这个值非常关键，hz系统的表单提交数据不涉及sf系统的流程实例id
                payentity.setThirdpartId(config.kuaidimycode);
                kc.completePaymoney(payentity);
        }
}