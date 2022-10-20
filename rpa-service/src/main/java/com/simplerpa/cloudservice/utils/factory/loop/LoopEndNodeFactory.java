package com.simplerpa.cloudservice.utils.factory.loop;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.loop.LoopEndNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class LoopEndNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public LoopEndNodeFactory(TaskNodeDetail nodeDetail){
        taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        return new LoopEndNode(taskNodeDetail);
    }
}
