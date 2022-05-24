package com.simplerpa.cloudservice.utils.factory.read;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.read.ReadCSVNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

import java.util.Base64;

public class ReadCSVNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public ReadCSVNodeFactory(TaskNodeDetail taskNodeDetail){
        this.taskNodeDetail = taskNodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        ReadCSVNode readCSVNode = new ReadCSVNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        Object file = params.get("file");
        Object fileName = params.get("fileName");
        Object outputParamName = params.get("outputParamName");
        if(file == null){
            throw new Exception(this.getClass().getName() + " : 没有文件信息！解析失败！");
        }
        if(fileName == null){
            throw new Exception(this.getClass().getName() + " : 没有文件名！解析失败！");
        }
        if(outputParamName == null){
            throw new Exception(this.getClass().getName() + " : 没有数据输出参数名称！解析失败！");
        }
        String[] files = ((String)file).split(",");
        readCSVNode.setFile(Base64.getDecoder().decode(files[1]));
        readCSVNode.setFileName((String) fileName);
        readCSVNode.setOutputParamName((String) outputParamName);
        return readCSVNode;
    }
}
