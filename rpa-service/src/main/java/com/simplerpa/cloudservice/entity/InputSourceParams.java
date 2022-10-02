package com.simplerpa.cloudservice.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;

public class InputSourceParams {
    private String parentSource;
    private String childSource;

    public InputSourceParams(){
        parentSource = null;
        childSource = null;
    }

    public InputSourceParams(String parentSource, String childSource){
        this.parentSource = parentSource;
        this.childSource = childSource;
    }

    public InputSourceParams(LinkedHashMap<String, String> linkedHashMap){
        setChildSource(linkedHashMap.get("childSource"));
        setParentSource(linkedHashMap.get("parentSource"));
    }

    public InputSourceParams(JSONObject jsonObject){
        setChildSource(jsonObject.getString("childSource"));
        setParentSource(jsonObject.getString("parentSource"));
    }

    public String getParentSource() {
        return parentSource;
    }

    public void setParentSource(String parentSource) {
        this.parentSource = parentSource;
    }

    public String getChildSource() {
        return childSource;
    }

    public void setChildSource(String childSource) {
        this.childSource = childSource;
    }
}
