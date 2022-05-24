package com.simplerpa.cloudservice.entity;

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
