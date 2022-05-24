package com.simplerpa.cloudservice.utils.factory.read;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.read.ReadTxtNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

import java.util.Base64;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年05月01日 16:41
 */

public class ReadTxtNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public ReadTxtNodeFactory(TaskNodeDetail taskNodeDetail){
        this.taskNodeDetail = taskNodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        ReadTxtNode readTxtNode = new ReadTxtNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        if(!params.containsKey("file")){
            throw new Exception(this.getClass().getName() + " : 没有文件信息！解析失败！");
        }else{
            String[] files = params.getString("file").split(",");
            readTxtNode.setFile(Base64.getDecoder().decode(files[1]));
        }
        if(!params.containsKey("fileName")){
            throw new Exception(this.getClass().getName() + " : 没有文件名！解析失败！");
        }else{
            readTxtNode.setFileName(params.getString("fileName"));
        }
        if(!params.containsKey("outputParamName")){
            throw new Exception(this.getClass().getName() + " : 没有数据输出参数名称！解析失败！");
        }else{
            readTxtNode.setOutputParamName(params.getString("outputParamName"));
        }
        return readTxtNode;
    }
}
