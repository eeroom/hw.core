package org.azeroth.workflow.viewModel;

public class DeployAdd {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefineKey() {
        return defineKey;
    }

    public void setDefineKey(String defineKey) {
        this.defineKey = defineKey;
    }

    public String getBizformId() {
        return bizformId;
    }

    public void setBizformId(String bizformId) {
        this.bizformId = bizformId;
    }

    public String getApproveformId() {
        return approveformId;
    }

    public void setApproveformId(String approveformId) {
        this.approveformId = approveformId;
    }

    String name;
    String defineKey;
    /**
     * 业务数据表单元素对应的id
     */
    String bizformId;
    /**
     * 审批操作对应的页面id
     */
    String approveformId;
}
