package com.simplerpa.cloudservice.entity;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月21日 21:23
 */

public class PanelTaskMessage {
    // 表示动作类型，如删除事件、新增事件等
    private Integer actionType;
    private Boolean nodeChanged;

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Boolean getNodeChanged() {
        return nodeChanged;
    }

    public void setNodeChanged(Boolean nodeChanged) {
        this.nodeChanged = nodeChanged;
    }
}
