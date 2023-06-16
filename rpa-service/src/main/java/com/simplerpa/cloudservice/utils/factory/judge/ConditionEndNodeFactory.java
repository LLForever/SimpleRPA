package com.simplerpa.cloudservice.utils.factory.judge;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.judge.ConditionEndNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class ConditionEndNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public ConditionEndNodeFactory(TaskNodeDetail nodeDetail){
        taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        return new ConditionEndNode(taskNodeDetail);
    }
}
