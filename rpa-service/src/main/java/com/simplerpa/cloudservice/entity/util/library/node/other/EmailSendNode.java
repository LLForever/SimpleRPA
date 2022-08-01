package com.simplerpa.cloudservice.entity.util.library.node.other;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.utils.EmailUtilSingleton;

public class EmailSendNode extends IRpaTaskNode {
    private String to, content;

    public EmailSendNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        EmailUtilSingleton instance = EmailUtilSingleton.getInstance();
        instance.sendMail(to, content);
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input){
        changeStringParams(to, input);
        changeStringParams(content, input);
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
