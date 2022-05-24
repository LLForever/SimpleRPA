package com.simplerpa.cloudservice.utils.factory.other;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.other.EmailSendNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class EmailSendNodeFactory implements RpaNodeFactory {
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
