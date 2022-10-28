package com.simplerpa.cloudservice.utils.factory.dataproc;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.dataproc.ReplaceTextNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class ReplaceTextNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public ReplaceTextNodeFactory(TaskNodeDetail nodeDetail){
        taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        ReplaceTextNode node = new ReplaceTextNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        String sourceStr = params.getString("source"), inputTextStr = params.getString("inputText");
        String targetText = params.getString("targetText");
        Boolean allReplace = params.getBoolean("allReplace");
        JSONObject source = JSONObject.parseObject(sourceStr);
        if(allReplace == null){
            throw new Exception(this.getClass().getName() + "缺少条件信息！");
        }
        if(StringUtils.isEmpty(sourceStr)){
            throw new Exception(this.getClass().getName() + "缺少输入参数信息！");
        }
        if(StringUtils.isEmpty(inputTextStr)){
            throw new Exception(this.getClass().getName() + "缺少输入内容信息！");
        }
        if (StringUtils.isEmpty(targetText)){
            throw new Exception(this.getClass().getName() + "缺少目标内容信息！");
        }
        node.setAllReplace(allReplace);
        node.setInputTextBck(inputTextStr);
        node.setInputText(inputTextStr);
        node.setTargetTextBck(targetText);
        node.setTargetText(targetText);
        node.setInputSource(new InputSourceParams(source));
        return node;
    }
}
