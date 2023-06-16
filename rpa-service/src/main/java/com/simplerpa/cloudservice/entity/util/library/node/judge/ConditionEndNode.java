package com.simplerpa.cloudservice.entity.util.library.node.judge;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

public class ConditionEndNode extends IRpaTaskNode {
    public ConditionEndNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) throws Exception {

    }

    @Override
    public void clearRpaOutput() {

    }
}
