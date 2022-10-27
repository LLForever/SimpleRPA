package com.simplerpa.cloudservice.utils.factory.loop;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.loop.ForLoopNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class ForLoopNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;
    private final Long taskId;

    public ForLoopNodeFactory(TaskNodeDetail nodeDetail, Long taskId){
        this.taskNodeDetail = nodeDetail;
        this.taskId = taskId;
    }


    @Override
    public IRpaTaskNode getInstance() throws Exception {
        ForLoopNode node = new ForLoopNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        String startPos = params.getString("startPos");
        String endPos = params.getString("endPos");
        String outputParamName = params.getString("outputParamName");
        if(StringUtils.isEmpty(startPos)){
            throw new Exception(this.getClass().getName() + "缺少循环起始位置信息！");
        }
        if(StringUtils.isEmpty(endPos)){
            throw new Exception(this.getClass().getName() + "缺少循环终止位置信息！");
        }
        if(StringUtils.isEmpty(outputParamName)){
            throw new Exception(this.getClass().getName() + " : 没有数据输出参数名称！解析失败！");
        }
        node.setStartPos(startPos);
        node.setEndPos(endPos);
        node.setOutputParamName(outputParamName);
        node.setTaskId(taskId);
        return node;
    }
}
