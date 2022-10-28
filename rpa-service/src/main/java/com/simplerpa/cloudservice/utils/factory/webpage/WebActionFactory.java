package com.simplerpa.cloudservice.utils.factory.webpage;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.SingleClickNode;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.WebAction;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

import java.util.LinkedHashMap;

public abstract class WebActionFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public WebActionFactory(TaskNodeDetail taskNodeDetail){
        this.taskNodeDetail = taskNodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        WebAction node = getNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        Object inputSource = params.get("inputSource");
        String xPath = params.getString("xPath");
        if(inputSource == null){
            throw new Exception(this.getClass().getName() + "缺少输入参数信息！");
        }
        if(StringUtils.isNotEmpty(xPath)){
            node.setxPath(xPath);
            node.setxPathBck(xPath);
        }
        node.setInputSourceParams(new InputSourceParams((LinkedHashMap<String, String>) inputSource));
        return node;
    }

    public abstract WebAction getNode(TaskNodeDetail taskNodeDetail) throws Exception;

    public TaskNodeDetail getTaskNodeDetail() {
        return taskNodeDetail;
    }
}
