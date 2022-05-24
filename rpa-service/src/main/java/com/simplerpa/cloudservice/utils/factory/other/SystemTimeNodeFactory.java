package com.simplerpa.cloudservice.utils.factory.other;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.other.SystemTimeNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class SystemTimeNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public SystemTimeNodeFactory(TaskNodeDetail detail){
        taskNodeDetail = detail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        SystemTimeNode systemTimeNode = new SystemTimeNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        Object outputParamName = params.get("outputParamName");
        if(outputParamName == null){
            throw new Exception(this.getClass().getName() + " : 没有数据输出参数名称！解析失败！");
        }
        systemTimeNode.setOutputParamName((String) outputParamName);
        return systemTimeNode;
    }
}
