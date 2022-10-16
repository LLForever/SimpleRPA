package com.simplerpa.cloudservice.utils.factory.aiEnhance;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.aiEnhance.ImageTableOcrNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

import java.util.LinkedHashMap;

public class ImageTableOcrNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public ImageTableOcrNodeFactory(TaskNodeDetail nodeDetail){
        taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        ImageTableOcrNode node = new ImageTableOcrNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        Object inputSource = params.get("inputSource");
        String outputParamName = params.getString("outputParamName");
        String[] outputAttributeList = params.getObject("outputAttributeList", String[].class);
        if(inputSource == null){
            throw new Exception(this.getClass().getName() + "缺少输入参数信息！");
        }
        if(StringUtils.isEmpty(outputParamName)){
            throw new Exception(this.getClass().getName() + " : 没有数据输出参数名称！解析失败！");
        }
        if(outputAttributeList == null || outputAttributeList.length == 0){
            throw new Exception(this.getClass().getName() + " : 没有待识别属性名信息！");
        }
        node.setInputSource(new InputSourceParams((LinkedHashMap<String, String>) inputSource));
        node.setOutputAttributeList(outputAttributeList);
        node.setOutputParamName(outputParamName);
        return node;
    }
}
