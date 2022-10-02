package com.simplerpa.cloudservice.entity.util.library.node.dataproc;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

import java.util.ArrayList;

public class AddTextNode extends IRpaTaskNode {
    private String inputText;
    private InputSourceParams inputSource;

    public AddTextNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(inputSource == null){
            throw new Exception(this.getClass().getName() + " : 缺少必要参数，执行失败！");
        }
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
            changeSingleResult(resultByParamName);
        }
        return null;
    }

    private void changeSingleResult(ArrayList<JSONObject> result){
        changeResult(result, DictionaryUtil.SINGLE_PARAM_FLAG);
    }

    private void changeResult(ArrayList<JSONObject> result, String target){
        for(JSONObject item : result){
            if(item.containsKey(target)){
                String str = item.getString(target);
                item.put(target, str + inputText);
            }
        }
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {
        inputText = changeStringParams(inputText, input);
    }

    public InputSourceParams getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSourceParams inputSource) {
        this.inputSource = inputSource;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }
}
