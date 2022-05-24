package com.simplerpa.cloudservice.utils.factory.other;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.other.TimestampToDateNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class TimestampToDateNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public TimestampToDateNodeFactory(TaskNodeDetail taskNodeDetail){
        this.taskNodeDetail = taskNodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        TimestampToDateNode node = new TimestampToDateNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        Object inputSource = params.get("inputSource");
        if(inputSource == null){
            throw new Exception(this.getClass().getName() + "缺少输入参数信息！");
        }
        node.setInputSource((InputSourceParams) inputSource);
        return node;
    }
}
