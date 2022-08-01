package com.simplerpa.cloudservice.utils.factory.webpage;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.SingleClickNode;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.WebAction;

public class SingleClickNodeFactory extends WebActionFactory {
    public SingleClickNodeFactory(TaskNodeDetail taskNodeDetail) {
        super(taskNodeDetail);
    }

    @Override
    public WebAction getNode(TaskNodeDetail taskNodeDetail) throws Exception {
        return new SingleClickNode(taskNodeDetail);
    }
}
