package com.simplerpa.cloudservice.entity.util.base;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;

import java.util.ArrayList;

public abstract class IRpaTaskNode {
    private static final String VARIABLE_FLAG = "{";
    protected TaskNodeDetail nodeDetail;
    public abstract RpaTaskOutput run(RpaTaskOutput input) throws Exception;
    public abstract void detectParamsValue(RpaTaskOutput input);

    protected String changeStringParams(String str, RpaTaskOutput output) {
        if(str == null){
            return str;
        }
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(str);
        } catch (Exception e) {
            return str;
        }
        if (jsonObject.containsKey("parentSource")) {
            ArrayList<JSONObject> resultByParamName = output.getResultByParamName(jsonObject.getString("parentSource"));
            String targetName = DictionaryUtil.SINGLE_PARAM_FLAG;
            if (jsonObject.containsKey("childSource")) {
                String childSource = jsonObject.getString("childSource");
                if(StringUtils.isNotEmpty(childSource)){
                    targetName = childSource;
                }
            }
            for (JSONObject json : resultByParamName) {
                if (json.containsKey(targetName)) {
                    str = json.getString(targetName);
                    break;
                }
            }

        }
        return str;
    }

    protected Object getObjectByParams(String str, RpaTaskOutput output){
        if(str == null){
            return str;
        }
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(str);
        } catch (Exception e) {
            return null;
        }
        if (jsonObject.containsKey("parentSource")) {
            ArrayList<JSONObject> resultByParamName = output.getResultByParamName(jsonObject.getString("parentSource"));
            String targetName = DictionaryUtil.SINGLE_PARAM_FLAG;
            if (jsonObject.containsKey("childSource")) {
                String childSource = jsonObject.getString("childSource");
                if(StringUtils.isNotEmpty(childSource)){
                    targetName = childSource;
                }
            }
            for (JSONObject json : resultByParamName) {
                if (json.containsKey(targetName)) {
                    return json.get(targetName);
                }
            }
        }
        return null;
    }

    public TaskNodeDetail getRpaTaskDetail(){
        return nodeDetail;
    }
}
