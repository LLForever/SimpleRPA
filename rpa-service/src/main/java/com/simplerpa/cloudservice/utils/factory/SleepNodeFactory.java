package com.simplerpa.cloudservice.utils.factory;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.SleepNode;

public class SleepNodeFactory implements RpaNodeFactory{
    private final TaskNodeDetail taskNodeDetail;

    public SleepNodeFactory(TaskNodeDetail nodeDetail){
        taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        SleepNode sleepNode = new SleepNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        Object sleepTime = params.get("sleepTime");
        Object unit = params.get("unit");
        if(sleepTime == null){
            throw new Exception(this.getClass().getName() + " : 休眠时间缺失！解析失败！");
        }
        sleepNode.setSleepTime((Long) sleepTime);
        sleepNode.setUnit((Integer) unit);
        return sleepNode;
    }
}
