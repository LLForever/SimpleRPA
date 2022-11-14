package com.simplerpa.cloudservice.utils.factory.webpage;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.WaitElementDisappearNode;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.WebAction;

public class WaitElementDisappearNodeFactory extends WebActionFactory{
    public WaitElementDisappearNodeFactory(TaskNodeDetail nodeDetail){
        super(nodeDetail);
    }

    @Override
    public WebAction getNode(TaskNodeDetail taskNodeDetail) throws Exception {
        return new WaitElementDisappearNode(taskNodeDetail);
    }
}
