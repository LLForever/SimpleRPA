package com.simplerpa.cloudservice.entity.util.library.node;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月29日 22:27
 */

public class StartNode extends IRpaTaskNode {
    public StartNode(){
        this.nodeDetail = new TaskNodeDetail();
        nodeDetail.setType("start");
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) {
        return null;
    }
}
