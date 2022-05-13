package com.simplerpa.cloudservice.entity;

import com.alibaba.fastjson.JSONObject;
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
    private JSONObject jsonObject;

    public PanelTaskMessage(Integer actionType, String message) {
        setActionType(actionType);
        setMessage(message);
    }

    public PanelTaskMessage(Integer actionType, JSONObject jsonObject){
        setActionType(actionType);
        setJsonObject(jsonObject);
    }

    public PanelTaskMessage(Integer actionType, JSONObject jsonObject, Boolean nodeChanged) {
        setActionType(actionType);
        setJsonObject(jsonObject);
        setNodeChanged(nodeChanged);
    }

    public Integer getActionType() {
        return actionType;
    }

    private void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Boolean getNodeChanged() {
        return nodeChanged;
    }

    private void setNodeChanged(Boolean nodeChanged) {
        this.nodeChanged = nodeChanged;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    private void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
