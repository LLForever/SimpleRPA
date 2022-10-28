package com.simplerpa.cloudservice.utils.factory.webpage;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.webpage.OpenWebPageNode;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

public class OpenWebPageNodeFactory implements RpaNodeFactory {
    private final TaskNodeDetail taskNodeDetail;

    public OpenWebPageNodeFactory(TaskNodeDetail nodeDetail){
        taskNodeDetail = nodeDetail;
    }

    @Override
    public IRpaTaskNode getInstance() throws Exception {
        OpenWebPageNode node = new OpenWebPageNode(taskNodeDetail);
        JSONObject params = taskNodeDetail.getParams();
        String url = getAvailableUrl(params.get("URL"));
        Object outputParamName = params.get("outputParamName");
        if(url == null){
            throw new Exception(this.getClass().getName() + " : 没有网页链接信息！解析失败！");
        }
        if(outputParamName == null){
            throw new Exception(this.getClass().getName() + " : 没有数据输出参数名称！解析失败！");
        }
        node.setURL(url);
        node.setURLBck(url);
        node.setOutputParamName((String) outputParamName);
        return node;
    }

    private String getAvailableUrl(Object URL){
        String url = (String) URL;
        if(StringUtils.isNotEmpty(url)){
            return url;
        }
        return null;
    }
}
