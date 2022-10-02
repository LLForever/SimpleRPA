package com.simplerpa.cloudservice.utils.factory.other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.other.DateToTimestampNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

import java.util.LinkedHashMap;

public class DateToTimestampNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public DateToTimestampNodeFactory(TaskNodeDetail nodeDetail){
        this.taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        DateToTimestampNode node = new DateToTimestampNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        String inputSource = params.getString("inputText");
        JSONObject input = JSON.parseObject(inputSource);
        if(StringUtils.isEmpty(inputSource)){
            throw new Exception(this.getClass().getName() + "缺少输入参数信息！");
        }
        node.setInputSource(new InputSourceParams(input));
        return node;
    }
}
