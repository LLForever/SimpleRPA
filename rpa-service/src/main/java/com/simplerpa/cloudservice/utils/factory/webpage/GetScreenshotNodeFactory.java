package com.simplerpa.cloudservice.utils.factory.webpage;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.GetScreenshotNode;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.WebAction;

public class GetScreenshotNodeFactory extends WebActionFactory{
    public GetScreenshotNodeFactory(TaskNodeDetail nodeDetail){
        super(nodeDetail);
    }

    @Override
    public WebAction getNode(TaskNodeDetail taskNodeDetail) throws Exception {
        return new GetScreenshotNode(taskNodeDetail);
    }
}
