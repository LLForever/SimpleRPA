package com.simplerpa.cloudservice.utils.factory.webpage;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.MouseHoverNode;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.WebAction;

public class MouseHoverNodeFactory extends WebActionFactory {

    public MouseHoverNodeFactory(TaskNodeDetail taskNodeDetail) {
        super(taskNodeDetail);
    }

    @Override
    public WebAction getNode(TaskNodeDetail taskNodeDetail) throws Exception {
        return new MouseHoverNode(taskNodeDetail);
    }
}
