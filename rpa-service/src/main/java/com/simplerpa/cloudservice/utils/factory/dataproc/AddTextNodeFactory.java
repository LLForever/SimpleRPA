package com.simplerpa.cloudservice.utils.factory.dataproc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.dataproc.AddTextNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class AddTextNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public AddTextNodeFactory(TaskNodeDetail nodeDetail){
        this.taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        AddTextNode node = new AddTextNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        String source = params.getString("source");
        String text = params.getString("inputText");
        JSONObject s = JSON.parseObject(source);
        if(StringUtils.isEmpty(source)){
            throw new Exception(this.getClass().getName() + "缺少输入参数信息！");
        }
        if(StringUtils.isEmpty(text)){
            throw new Exception(this.getClass().getName() + "缺少追加文本信息！");
        }
        node.setInputSource(new InputSourceParams(s));
        node.setInputText(text);
        node.setInputTextBck(text);
        return node;
    }
}
