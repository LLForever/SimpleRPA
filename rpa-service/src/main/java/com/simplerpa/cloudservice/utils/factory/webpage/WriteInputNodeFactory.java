package com.simplerpa.cloudservice.utils.factory.webpage;

import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.WebAction;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.WriteInputNode;

public class WriteInputNodeFactory extends WebActionFactory{
    public WriteInputNodeFactory(TaskNodeDetail taskNodeDetail) {
        super(taskNodeDetail);
    }

    @Override
    public WebAction getNode(TaskNodeDetail taskNodeDetail) throws Exception {
        WriteInputNode node = new WriteInputNode(taskNodeDetail);
        String inputText = getTaskNodeDetail().getParams().getString("inputText");
        if(StringUtils.isEmpty(inputText)){
            throw new Exception(this.getClass().getName() + "缺少输入内容！");
        }
        node.setInputText(inputText);
        return node;
    }
}
