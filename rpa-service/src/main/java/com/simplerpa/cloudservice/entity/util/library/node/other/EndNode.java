package com.simplerpa.cloudservice.entity.util.library.node.other;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

public class EndNode extends IRpaTaskNode {
    public EndNode(){
        this.nodeDetail = new TaskNodeDetail();
        nodeDetail.setType("end");
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {

    }
}
