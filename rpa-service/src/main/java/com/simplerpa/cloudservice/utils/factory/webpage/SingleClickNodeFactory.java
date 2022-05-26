package com.simplerpa.cloudservice.utils.factory.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.SingleClickNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

import java.util.LinkedHashMap;

public class SingleClickNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public SingleClickNodeFactory(TaskNodeDetail taskNodeDetail){
        this.taskNodeDetail = taskNodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        SingleClickNode node = new SingleClickNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        Object inputSource = params.get("inputSource");
        Object xPath = params.get("xPath");
        if(inputSource == null){
            throw new Exception(this.getClass().getName() + "缺少输入参数信息！");
        }
        if(xPath == null){
            throw new Exception(this.getClass().getName() + "缺少xPath信息！");
        }
        node.setxPath((String) xPath);
        node.setInputSourceParams(new InputSourceParams((LinkedHashMap<String, String>) inputSource));
        return node;
    }
}
