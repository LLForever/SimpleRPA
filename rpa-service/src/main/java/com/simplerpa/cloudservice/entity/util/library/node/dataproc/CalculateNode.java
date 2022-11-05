package com.simplerpa.cloudservice.entity.util.library.node.dataproc;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

public class CalculateNode extends IRpaTaskNode {
    private String targetText, outputParamName, source;
    private String targetTextBck, sourceBck;
    private Integer calculateType;
    private RpaTaskOutput output;

    private static final Integer PLUS = 0, MINUS = 1, MULTI = 2, DIV = 3;

    public CalculateNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        detectParamsValue(input);
        Double targetNum = Double.valueOf(targetText);
        Double sourceNum = Double.valueOf(source);
        JSONObject jsonObject = new JSONObject();
        if(PLUS.equals(calculateType)){
            jsonObject.put(DictionaryUtil.SINGLE_PARAM_FLAG, targetNum + sourceNum);
        }else if(MINUS.equals(calculateType)){
            jsonObject.put(DictionaryUtil.SINGLE_PARAM_FLAG, sourceNum - targetNum);
        }else if(MULTI.equals(calculateType)){
            jsonObject.put(DictionaryUtil.SINGLE_PARAM_FLAG, sourceNum * targetNum);
        }else if(DIV.equals(calculateType)){
            jsonObject.put(DictionaryUtil.SINGLE_PARAM_FLAG, sourceNum / targetNum);
        }else{
            return null;
        }
        addOutput(jsonObject);
        return output;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) throws Exception {
        targetText = transformParams(targetText, targetTextBck, input);
        source = transformParams(source, sourceBck, input);
    }

    @Override
    public void clearRpaOutput() {
        output = new RpaTaskOutput();
    }

    private void addOutput(JSONObject jsonObject){
        if(output == null){
            output = new RpaTaskOutput();
        }
        output.addObject(outputParamName, jsonObject);
    }

    public String getTargetText() {
        return targetText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
    }

    public String getOutputParamName() {
        return outputParamName;
    }

    public void setOutputParamName(String outputParamName) {
        this.outputParamName = outputParamName;
    }

    public Integer getCalculateType() {
        return calculateType;
    }

    public void setCalculateType(Integer calculateType) {
        this.calculateType = calculateType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceBck() {
        return sourceBck;
    }

    public void setSourceBck(String sourceBck) {
        this.sourceBck = sourceBck;
    }

    public String getTargetTextBck() {
        return targetTextBck;
    }

    public void setTargetTextBck(String targetTextBck) {
        this.targetTextBck = targetTextBck;
    }
}
