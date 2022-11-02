package com.simplerpa.cloudservice.entity.util.library.node.aiEnhance;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.RpaTools;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.tools.AiEnhanceTool;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;

public class KeyWordExtraNode extends IRpaTaskNode {
    private String outputParamName;
    private RpaTaskOutput output;
    private InputSourceParams inputSource;
    String[] outputAttributeList;

    public KeyWordExtraNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(StringUtils.isEmpty(inputSource.getParentSource()) || StringUtils.isEmpty(inputSource.getChildSource())){
            throw new Exception(this.getClass().getName() + " : 缺少指定的父节点或者子节点信息！");
        }
        ArrayList<JSONObject> resultByParamName = input.getResultByParamName(inputSource.getParentSource());
        for(JSONObject jsonObject : resultByParamName) {
            if (jsonObject.containsKey(inputSource.getChildSource())) {
                WebElement element = jsonObject.getObject(inputSource.getChildSource(), WebElement.class);
                WebElement img = element;
                if(!"img".equals(element.getTagName())){
                    img = element.findElement(By.tagName("img"));
                }
                if(img != null){
                    String src = img.getAttribute("src");
                    JSONObject ocrResult = AiEnhanceTool.getAiResult(src, AiEnhanceTool.KEY_EXT, Arrays.asList(outputAttributeList));
                    JSONArray res = ocrResult.getJSONArray("res");
                    getAttributeValue(res);
                }
            }else if(jsonObject.containsKey(DictionaryUtil.SINGLE_PARAM_FLAG)){
                try {
                    String string = jsonObject.getString(DictionaryUtil.SINGLE_PARAM_FLAG);
                    if(RpaTools.isURLValid(string)){
                        JSONObject aiResult = AiEnhanceTool.getAiResult(string, AiEnhanceTool.KEY_EXT, Arrays.asList(outputAttributeList));
                        JSONArray res = aiResult.getJSONArray("res");
                        getAttributeValue(res);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return output;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) throws Exception {

    }

    @Override
    public void clearRpaOutput() {
        output = new RpaTaskOutput();
    }

    private void getAttributeValue(JSONArray jsonArray){
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        for (String key : jsonObject.keySet()){
            JSONArray jsonArray1 = jsonObject.getJSONArray(key);
            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
            JSONObject o = new JSONObject();
            o.put(key, jsonObject1.getString("text"));
            addOutput(o);
        }
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

    public String[] getOutputAttributeList() {
        return outputAttributeList;
    }

    public void setOutputAttributeList(String[] outputAttributeList) {
        this.outputAttributeList = outputAttributeList;
    }
}
