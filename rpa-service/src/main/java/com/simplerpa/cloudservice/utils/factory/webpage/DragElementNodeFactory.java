package com.simplerpa.cloudservice.utils.factory.webpage;

import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.DragElementNode;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.WebAction;

public class DragElementNodeFactory extends WebActionFactory{

    public DragElementNodeFactory(TaskNodeDetail taskNodeDetail) {
        super(taskNodeDetail);
    }

    @Override
    public WebAction getNode(TaskNodeDetail taskNodeDetail) throws Exception {
        DragElementNode dragElementNode = new DragElementNode(taskNodeDetail);
        String targetPosition = getTaskNodeDetail().getParams().getString("targetPosition");
        if(StringUtils.isEmpty(targetPosition)){
            throw new Exception(this.getClass().getName() + "缺少目标位置信息！");
        }
        dragElementNode.setTargetPosition(targetPosition);
        return dragElementNode;
    }
}
