package org.azeroth.workflow.bpm;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;

/**
 * 设置审批人，
 * 用法：把这个类设置为UserTask的Task Listener,然后设置事件类型为create,
 * 已知问题：设置事件类型为assignment是不行的
 */
public class SetAssigneeListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
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
}
