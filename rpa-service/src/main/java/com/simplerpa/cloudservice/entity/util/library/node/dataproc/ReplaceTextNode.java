package com.simplerpa.cloudservice.entity.util.library.node.dataproc;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

import java.util.ArrayList;

public class ReplaceTextNode extends IRpaTaskNode {
    private String inputText, inputTextBck;
    private String targetText, targetTextBck;
    private InputSourceParams inputSource;
    private boolean allReplace;

    public ReplaceTextNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        detectParamsValue(input);
        String childSource = inputSource.getChildSource();
        String parentSource = inputSource.getParentSource();
        if(parentSource == null){
            throw new Exception(this.getClass().getName() + "缺少输入参数");
        }
        ArrayList<JSONObject> resultByParamName = input.getResultByParamName(parentSource);
        if(StringUtils.isNotEmpty(childSource)){
            changeResult(resultByParamName, childSource);
        }else{
            changeResult(resultByParamName);
        }
        return null;
    }

    private void changeResult(ArrayList<JSONObject> result, String target){
        for(JSONObject item : result){
            if(item.containsKey(target)){
                String str = item.getString(target);
                if(allReplace){
                    str = str.replace(inputText, targetText);
                }else{
                    str = str.replaceFirst(inputText, targetText);
                }
                item.put(target, str);
            }
        }
    }

    private void changeResult(ArrayList<JSONObject> result) {
        changeResult(result, DictionaryUtil.SINGLE_PARAM_FLAG);
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) throws Exception {
        inputText = transformParams(inputText, inputTextBck, input);
        targetText = transformParams(targetText, targetTextBck,input);
    }

    @Override
    public void clearRpaOutput() {

    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getTargetText() {
        return targetText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
    }

    public InputSourceParams getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSourceParams inputSource) {
        this.inputSource = inputSource;
    }

    public boolean isAllReplace() {
        return allReplace;
    }

    public void setAllReplace(boolean allReplace) {
        this.allReplace = allReplace;
    }

    public String getInputTextBck() {
        return inputTextBck;
    }

    public void setInputTextBck(String inputTextBck) {
        this.inputTextBck = inputTextBck;
    }

    public String getTargetTextBck() {
        return targetTextBck;
    }

    public void setTargetTextBck(String targetTextBck) {
        this.targetTextBck = targetTextBck;
    }
}