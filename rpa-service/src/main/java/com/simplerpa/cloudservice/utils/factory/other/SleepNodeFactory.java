package com.simplerpa.cloudservice.utils.factory.other;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.other.SleepNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class SleepNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public SleepNodeFactory(TaskNodeDetail nodeDetail){
        taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        SleepNode sleepNode = new SleepNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        String sleepTime = params.getString("sleepTime");
        String unit = params.getString("unit");
        if(StringUtils.isEmpty(sleepTime)){
            throw new Exception(this.getClass().getName() + " : 休眠时间缺失！解析失败！");
        }
        sleepNode.setSleepTime(Long.valueOf(sleepTime));
        sleepNode.setUnit(Integer.valueOf(unit));
        return sleepNode;
    }
}
