package com.simplerpa.cloudservice.entity.util.library.node;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

public class DateToTimestampNode implements IRpaTaskNode {
    private final TaskNodeDetail nodeDetail;
    private JSONObject paramSource;
    private String outputParamName;
    private RpaTaskOutput output;

    public DateToTimestampNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(paramSource == null || outputParamName == null){
            throw new Exception(this.getClass().getName() + " : 缺少必要参数，执行失败！");
        }
        return null;
    }

    @Override
    public TaskNodeDetail getRpaTaskDetail() {
        return nodeDetail;
    }

    public JSONObject getParamSource() {
        return paramSource;
    }

    public void setParamSource(JSONObject paramSource) {
        this.paramSource = paramSource;
    }

    public String getOutputParamName() {
        return outputParamName;
    }

    public void setOutputParamName(String outputParamName) {
        this.outputParamName = outputParamName;
    }
}
