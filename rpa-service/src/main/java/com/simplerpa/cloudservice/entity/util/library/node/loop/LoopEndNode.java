package com.simplerpa.cloudservice.entity.util.library.node.loop;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

public class LoopEndNode extends IRpaTaskNode {
    public LoopEndNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {

    }

    @Override
    public void clearRpaOutput() {

    }
}
