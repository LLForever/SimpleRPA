package com.simplerpa.cloudservice.entity.util.library.node.dataproc;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

import java.util.ArrayList;

public class TextLengthNode extends IRpaTaskNode {
    private String outputParamName;
    private RpaTaskOutput output;
    private InputSourceParams inputSource;

    public TextLengthNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        String childSource = inputSource.getChildSource();
        String parentSource = inputSource.getParentSource();
        if(parentSource == null){
            throw new Exception(this.getClass().getName() + "缺少输入参数");
        }
        String text;
        ArrayList<JSONObject> resultByParamName = input.getResultByParamName(parentSource);
        if(StringUtils.isNotEmpty(childSource)){
            text = getInputString(resultByParamName, childSource);
        }else{
            text = getInputString(resultByParamName);
        }
        if(text == null){
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DictionaryUtil.SINGLE_PARAM_FLAG, text.length());
        addOutput(jsonObject);
        return output;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {

    }

    @Override
    public void clearRpaOutput() {
        output = new RpaTaskOutput();
    }

    private String getInputString(ArrayList<JSONObject> result){
        return getInputString(result, DictionaryUtil.SINGLE_PARAM_FLAG);
    }

    private String getInputString(ArrayList<JSONObject> result, String target){
        for(JSONObject item : result){
            if(item.containsKey(target)){
                String str = item.getString(target);
                return str;
            }
        }
        return null;
    }

    private void addOutput(JSONObject jsonObject){
        if(output == null){
            output = new RpaTaskOutput();
        }
        output.addObject(outputParamName, jsonObject);
    }

    public String getOutputParamName() {
        return outputParamName;
    }

    public void setOutputParamName(String outputParamName) {
        this.outputParamName = outputParamName;
    }

    public InputSourceParams getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSourceParams inputSource) {
        this.inputSource = inputSource;
    }
}
