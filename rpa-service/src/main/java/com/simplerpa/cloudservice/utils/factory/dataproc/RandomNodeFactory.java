package com.simplerpa.cloudservice.utils.factory.dataproc;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.dataproc.RandomNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class RandomNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public RandomNodeFactory(TaskNodeDetail nodeDetail){
        this.taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        RandomNode node = new RandomNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        if(!params.containsKey("outputParamName")){
            throw new Exception(this.getClass().getName() + " : 没有数据输出参数名称！解析失败！");
        }else{
            node.setOutputParamName(params.getString("outputParamName"));
        }
        return node;
    }
}
