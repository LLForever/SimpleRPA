package com.simplerpa.cloudservice.entity.util.base;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;

import java.util.ArrayList;

public abstract class IRpaTaskNode {
    protected TaskNodeDetail nodeDetail;
    public abstract RpaTaskOutput run(RpaTaskOutput input) throws Exception;
    public abstract void detectParamsValue(RpaTaskOutput input) throws Exception;
    public abstract void clearRpaOutput();

    private String changeStringParams(String str, RpaTaskOutput output) {
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
            String targetName;
            if (jsonObject.containsKey("childSource")) {
                String childSource = jsonObject.getString("childSource");
                if(StringUtils.isNotEmpty(childSource)){
                    targetName = childSource;
                    for (JSONObject json : resultByParamName) {
                        if (json.containsKey(targetName)) {
                            return json.get(targetName);
                        }
                    }
                }
            }
            return resultByParamName;
        }
        return null;
    }

    protected String transformParams(String str, String str_bck, RpaTaskOutput output) throws Exception {
        if(str == null && str_bck == null){
            return str;
        }
        JSONObject jsonObject;
        try {
            jsonObject = JSONObject.parseObject(str_bck);
            if (!jsonObject.containsKey("parentSource")) {
                throw new Exception("参数源中没有父节点信息，请检查是否正确配置！");
            }
        } catch (Exception e) {
            return str;
        }
        String parentSource = jsonObject.getString("parentSource");
        Integer oldLayer, curLayer;
        try {
            oldLayer = output.getLayerByParams(parentSource);
            curLayer = output.getLayer();
        }catch (Exception e){
            throw new Exception("找不到参数源配置的信息，请检查！");
        }
        if(curLayer < oldLayer){
            throw new Exception("参数源无法读取！");
        }
        if(oldLayer.equals(curLayer)){
            return changeStringParams(str_bck, output);
        }else{
            return changeStringParams(str, output);
        }
    }

    public TaskNodeDetail getRpaTaskDetail(){
        return nodeDetail;
    }
}
