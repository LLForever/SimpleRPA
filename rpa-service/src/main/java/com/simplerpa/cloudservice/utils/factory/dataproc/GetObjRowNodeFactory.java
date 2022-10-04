package com.simplerpa.cloudservice.utils.factory.dataproc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.dataproc.GetObjRowNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class GetObjRowNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public GetObjRowNodeFactory(TaskNodeDetail nodeDetail){
        this.taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        GetObjRowNode node = new GetObjRowNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        String outputParam = params.getString("outputParamName");
        String sourceText = params.getString("source");
        Integer rowNum = params.getInteger("rowNum");
        JSONObject source = JSON.parseObject(sourceText);
        if(rowNum == null){
            throw new Exception(this.getClass().getName() + " : 没有填写数组指定行数！");
        }
        if(StringUtils.isEmpty(outputParam)){
            throw new Exception(this.getClass().getName() + " : 没有数据输出参数名称！解析失败！");
        }
        if(StringUtils.isEmpty(sourceText)){
            throw new Exception(this.getClass().getName() + "缺少输入参数信息！");
        }
        node.setOutputParamName(outputParam);
        node.setRowNum(rowNum);
        node.setInputSource(new InputSourceParams(source));
        return node;
    }
}
