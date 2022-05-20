package com.simplerpa.cloudservice.entity.util.library.node;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

public class TimestampToDateNode implements IRpaTaskNode {
    private final TaskNodeDetail nodeDetail;

    public TimestampToDateNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        return null;
    }

    @Override
    public TaskNodeDetail getRpaTaskDetail() {
        return null;
    }
}
