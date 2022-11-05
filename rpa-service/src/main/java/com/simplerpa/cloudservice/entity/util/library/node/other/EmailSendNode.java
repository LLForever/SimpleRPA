package com.simplerpa.cloudservice.entity.util.library.node.other;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.utils.EmailUtilSingleton;

public class EmailSendNode extends IRpaTaskNode {
    private String to, content, subject;
    private String toBck, contentBck, subjectBck;

    public EmailSendNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        detectParamsValue(input);
        EmailUtilSingleton instance = EmailUtilSingleton.getInstance();
        instance.sendMail(to, content, subject);
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) throws Exception {
        to = transformParams(to, toBck, input);
        content = transformParams(content, contentBck, input);
        subject = transformParams(subject, subjectBck, input);
    }

    @Override
    public void clearRpaOutput() {

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

    public String getToBck() {
        return toBck;
    }

    public void setToBck(String toBck) {
        this.toBck = toBck;
    }

    public String getContentBck() {
        return contentBck;
    }

    public void setContentBck(String contentBck) {
        this.contentBck = contentBck;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectBck() {
        return subjectBck;
    }

    public void setSubjectBck(String subjectBck) {
        this.subjectBck = subjectBck;
    }
}
