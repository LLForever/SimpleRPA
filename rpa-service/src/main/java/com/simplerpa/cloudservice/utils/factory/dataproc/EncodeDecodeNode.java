package com.simplerpa.cloudservice.utils.factory.dataproc;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

import java.util.Base64;

public class EncodeDecodeNode extends IRpaTaskNode {
    public EncodeDecodeNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {

    }
}
