package com.simplerpa.cloudservice.entity.util.library.node.dataproc;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

public class GenerateTextNode extends IRpaTaskNode {
    private String inputText, outputParamName;
    private RpaTaskOutput output;

    public GenerateTextNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DictionaryUtil.SINGLE_PARAM_FLAG, inputText);
        addOutput(jsonObject);
        return output;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {

    }

    private void addOutput(JSONObject jsonObject){
        if(output == null){
            output = new RpaTaskOutput();
        }
        output.addObject(outputParamName, jsonObject);
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getOutputParamName() {
        return outputParamName;
    }

    public void setOutputParamName(String outputParamName) {
        this.outputParamName = outputParamName;
    }
}
