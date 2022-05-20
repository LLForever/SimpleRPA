package com.simplerpa.cloudservice.utils.factory;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.EmailSendNode;

public class EmailSendNodeFactory implements RpaNodeFactory{
    private final TaskNodeDetail taskNodeDetail;

    public EmailSendNodeFactory(TaskNodeDetail nodeDetail){
        taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        EmailSendNode node = new EmailSendNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        return node;
    }
}
