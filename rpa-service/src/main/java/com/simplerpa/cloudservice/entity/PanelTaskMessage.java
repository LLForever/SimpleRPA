package com.simplerpa.cloudservice.entity;

import com.simplerpa.cloudservice.entity.util.DictionaryUtil;

import java.io.Serializable;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月21日 21:23
 */

public class PanelTaskMessage implements Serializable {
    // 表示动作类型，如删除事件、新增事件等
    private Integer actionType;
    private Boolean nodeChanged;
    private String message;

    public PanelTaskMessage(){
        actionType = DictionaryUtil.TASK_MESSAGE_OK;
        setMessage("Connection is created successfully!");
    }

    public PanelTaskMessage(Integer actionType){
        setActionType(actionType);
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
