package com.simplerpa.cloudservice.utils.factory.dataproc;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.dataproc.GenerateTextNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class GenerateTextNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public GenerateTextNodeFactory(TaskNodeDetail nodeDetail){
        taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        GenerateTextNode node = new GenerateTextNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        String inputText = params.getString("inputText");
        String outputParamName = params.getString("outputParamName");
        if(inputText == null){
            inputText = "";
        }
        if(StringUtils.isEmpty(outputParamName)){
            throw new Exception(this.getClass().getName() + " : 没有数据输出参数名称！解析失败！");
        }
        node.setInputText(inputText);
        node.setOutputParamName(outputParamName);
        return node;
    }
}
