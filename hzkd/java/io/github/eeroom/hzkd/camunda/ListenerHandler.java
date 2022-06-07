package io.github.eeroom.hzkd.camunda;

import io.github.eeroom.hzkd.MyObjectFacotry;
import io.github.eeroom.hzkd.MyDbContext;
import io.github.eeroom.hzkd.db.bizdata;
import io.github.eeroom.hzkd.db.bizdatasub;
import io.github.eeroom.hzkd.viewmodel.BizDataStatus;
import io.github.eeroom.hzkd.viewmodel.TaskStatus;
import io.github.eeroom.nalu.Columns;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ListenerHandler implements Serializable {
    static final long serialVersionUID = 42L;

    public void setAssignee(DelegateTask delegateTask) {
        //变量名称findAssigneeMode这个是互相约定好，后续UserTask使用这个Listener就添加这个名称的变量
        var findAssigneeMode = delegateTask.getVariable("findAssigneeMode");//按角色，等等
        var findAssigneeTarget = delegateTask.getVariable("findAssigneeTarget");//直接主管,总经理，等等
        //getVariableLocal取不到UserTask上定义的变量
        var findAssigneeModeLocal = delegateTask.getVariableLocal("findAssigneeMode");//按角色，等等
        //获取UserTask上的Extensions定义的数据
        //var lst= this.getExtensionValue(delegateTask);
        String assignee="";
        if(findAssigneeMode.equals("按角色")){
            if(findAssigneeTarget.equals("直接主管"))
                assignee="张三2";
            else if(findAssigneeTarget.equals("总监"))
                assignee="廖总";
            else if(findAssigneeTarget.equals("总经理"))
                assignee="徐总";
            else if(findAssigneeTarget.equals("HR"))
                assignee="胡蓉";
        }else if(findAssigneeMode.equals("直接指定"))
            assignee=findAssigneeTarget.toString();
        if(org.springframework.util.StringUtils.isEmpty(assignee))
            throw  new IllegalArgumentException(String.format("查找审批人失败，findAssigneeMode=%s,findAssigneeTarget=%s",findAssigneeMode,findAssigneeTarget));
        delegateTask.setAssignee(assignee);
    }

    /**
     * 获取UserTask上的Extensions定义的数据
     * @param delegateTask
     * @return
     */
    private List<?> getExtensionValue(DelegateTask delegateTask) {
        var lst = delegateTask.getBpmnModelElementInstance()
                .getExtensionElements()
                .getElementsQuery()
                .filterByType(CamundaProperties.class)
                .singleResult()
                .getCamundaProperties()
                .stream()
                .map(x -> new Map.Entry() {

                    @Override
                    public Object getKey() {
                        return x.getCamundaName();
                    }

                    @Override
                    public Object getValue() {
                        return x.getCamundaValue();
                    }

                    @Override
                    public Object setValue(Object o) {
                        return null;
                    }
                }).collect(java.util.stream.Collectors.toList());
        return lst;
    }

    /**
     * 可以取代AssigneeHook
     * @param delegateTask
     */
    public void syncAssigneeToBizdatasub(DelegateTask delegateTask) {
        var processId=delegateTask.getProcessInstanceId();
        var taskId=delegateTask.getId();
        var dbcontext= MyObjectFacotry.getBean(MyDbContext.class);
        var lstass= delegateTask.getAssignee().split(",");
        var lstbizdatasub= Arrays.stream(lstass).map(x->{
            var tmp=new bizdatasub();
            tmp.setassignee(x);
            tmp.setprocessId(processId);
            tmp.settaskId(taskId);
            tmp.setassigneeCompleted(TaskStatus.处理中);
            tmp.setstatus(TaskStatus.处理中);
            return tmp;
        }).collect(Collectors.toList());
        dbcontext.add(lstbizdatasub)
                .setInsertCol(x-> Columns.of(x.getassignee(),x.getprocessId(),x.gettaskId(),x.getassigneeCompleted(),x.getstatus()));
    }

    /**
     * 可以取代BizdataStatusSetter
     * @param delegateTask
     */
    public void updateBizdataStatusByTask(DelegateTask delegateTask,String status) {
        var pid= delegateTask.getProcessInstanceId();
        this.updateBizdataStatus(pid,status);
    }

    private void updateBizdataStatus(String pid, String status) {
        var dbcontext= MyObjectFacotry.getBean(MyDbContext.class);
        var bizd= dbcontext.dbSet(bizdata.class).select()
                .where(x->x.col(a->a.getprocessId()).eq(pid))
                .firstOrDefault();
        if(bizd==null)
            throw new RuntimeException("没有找到对应的bizdata,processId:"+pid);
        var value= BizDataStatus.valueOf(status);
        dbcontext.edit(bizdata.class)
                .setUpdateCol(x->x.getstatus(),value)
                .where(x->x.col(a->a.getprocessId()).eq(bizd.getprocessId()));
    }

    public void updateBizdataStatusByExecution(DelegateExecution delegateExecution, String status) {
        var pid= delegateExecution.getProcessInstanceId();
        this.updateBizdataStatus(pid,status);
    }

    public String concat(String... value){
        return String.join("",value);
    }
}
