package com.simplerpa.cloudservice.utils.factory.judge;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.judge.SingleConditionNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class SingleConditionNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public SingleConditionNodeFactory(TaskNodeDetail nodeDetail){
        taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        SingleConditionNode node = new SingleConditionNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        String inputText = params.getString("inputText");
        String targetText = params.getString("targetText");
        String judgeType = params.getString("judgeType");
        if(StringUtils.isEmpty(inputText)){
            throw new Exception(this.getClass().getName() + "缺少输入信息！");
        }
        if(StringUtils.isEmpty(judgeType)){
            throw new Exception(this.getClass().getName() + "缺少追加判断类型信息！");
        }
        node.setInputText(inputText);
        node.setJudgeType(judgeType);
        node.setTargetText(targetText);
        return node;
    }
}
