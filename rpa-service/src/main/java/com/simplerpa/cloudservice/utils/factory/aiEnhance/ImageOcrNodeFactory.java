package com.simplerpa.cloudservice.utils.factory.aiEnhance;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.aiEnhance.ImageOcrNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

import java.util.LinkedHashMap;

public class ImageOcrNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public ImageOcrNodeFactory(TaskNodeDetail nodeDetail){
        taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        ImageOcrNode node = new ImageOcrNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        Object inputSource = params.get("inputSource");
        String outputParamName = params.getString("outputParamName");
        if(inputSource == null){
            throw new Exception(this.getClass().getName() + "缺少输入参数信息！");
        }
        if(StringUtils.isEmpty(outputParamName)){
            throw new Exception(this.getClass().getName() + " : 没有数据输出参数名称！解析失败！");
        }
        node.setInputSource(new InputSourceParams((LinkedHashMap<String, String>) inputSource));
        node.setOutputParamName(outputParamName);
        return node;
    }
}
