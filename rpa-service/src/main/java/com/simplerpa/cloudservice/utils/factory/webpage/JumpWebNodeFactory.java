package com.simplerpa.cloudservice.utils.factory.webpage;

import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.JumpWebNode;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.WebAction;

public class JumpWebNodeFactory extends WebActionFactory{
    public JumpWebNodeFactory(TaskNodeDetail taskNodeDetail) {
        super(taskNodeDetail);
    }

    @Override
    public WebAction getNode(TaskNodeDetail taskNodeDetail) throws Exception {
        JumpWebNode node = new JumpWebNode(taskNodeDetail);
        String url = getTaskNodeDetail().getParams().getString("URL");
        if(StringUtils.isEmpty(url)){
            throw new Exception(this.getClass().getName() + "URL不能为空");
        }
        node.setURL(url);
        return node;
    }
}
