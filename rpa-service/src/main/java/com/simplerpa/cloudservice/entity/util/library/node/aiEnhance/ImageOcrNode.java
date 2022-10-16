package com.simplerpa.cloudservice.entity.util.library.node.aiEnhance;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.tools.AiEnhanceTool;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;

public class ImageOcrNode extends IRpaTaskNode {
    private String outputParamName;
    private RpaTaskOutput output;
    private InputSourceParams inputSource;

    public ImageOcrNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(StringUtils.isEmpty(inputSource.getParentSource()) || StringUtils.isEmpty(inputSource.getChildSource())){
            throw new Exception(this.getClass().getName() + " : 缺少指定的父节点或者子节点信息！");
        }
        ArrayList<JSONObject> resultByParamName = input.getResultByParamName(inputSource.getParentSource());
        for(JSONObject jsonObject : resultByParamName){
            if(jsonObject.containsKey(inputSource.getChildSource())){
                WebElement element = jsonObject.getObject(inputSource.getChildSource(), WebElement.class);
                WebElement img = element;
                if(!"img".equals(element.getTagName())){
                    img = element.findElement(By.tagName("img"));
                }
                if(img != null){
                    String src = img.getAttribute("src");
                    JSONObject ocrResult = AiEnhanceTool.getOcrResult(src);
                    JSONArray array = ocrResult.getJSONArray("res");
                    StringBuilder ocrStr = new StringBuilder();
                    for (int j = 0; j < array.size(); j++) {
                        JSONObject object = array.getObject(j, JSONObject.class);
                        ocrStr.append(object.getString("text"));
                    }
                    JSONObject obj = new JSONObject();
                    obj.put(DictionaryUtil.SINGLE_PARAM_FLAG, ocrStr.toString());
                    addOutput(obj);
                }
                break;
            }
        }
        return output;
    }

    private void addOutput(JSONObject jsonObject){
        if(output == null){
            output = new RpaTaskOutput();
        }
        output.addObject(outputParamName, jsonObject);
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {

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
