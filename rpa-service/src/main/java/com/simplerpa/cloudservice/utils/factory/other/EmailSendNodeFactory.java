package com.simplerpa.cloudservice.utils.factory.other;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
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
        String to = params.getString("to");
        String content = params.getString("content");
        if(StringUtils.isEmpty(to)){
            throw new Exception(this.getClass().getName() + " : 缺少收件人！执行失败");
        }
        if(StringUtils.isEmpty(content)){
            throw new Exception(this.getClass().getName() + " : 缺少邮件内容！执行失败");
        }
        node.setContent(content);
        node.setTo(to);
        return node;
    }
}
