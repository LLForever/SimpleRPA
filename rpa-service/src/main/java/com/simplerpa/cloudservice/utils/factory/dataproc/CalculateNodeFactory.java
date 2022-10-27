package com.simplerpa.cloudservice.utils.factory.dataproc;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.dataproc.CalculateNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class CalculateNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public CalculateNodeFactory(TaskNodeDetail nodeDetail){
        taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        CalculateNode node = new CalculateNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        String source = params.getString("source");
        String outputParamName = params.getString("outputParamName");
        Integer calculateType = params.getInteger("calculateType");
        String targetText = params.getString("targetText");
        if(StringUtils.isEmpty(outputParamName)){
            throw new Exception(this.getClass().getName() + " : 没有数据输出参数名称！解析失败！");
        }
        if(StringUtils.isEmpty(source)){
            throw new Exception(this.getClass().getName() + " : 没有输入源！解析失败！");
        }
        if(calculateType != null){
            throw new Exception(this.getClass().getName() + " : 没有计算类型！解析失败！");
        }
        if(StringUtils.isEmpty(targetText)){
            throw new Exception(this.getClass().getName() + " : 没有计算目标值！解析失败！");
        }
        node.setCalculateType(calculateType);
        node.setOutputParamName(outputParamName);
        node.setTargetText(targetText);
        node.setSource(source);
        return node;
    }
}
