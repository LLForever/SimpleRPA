package com.simplerpa.cloudservice.entity.util.library.node.aiEnhance;

import cn.hutool.core.util.StrUtil;
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

import java.util.ArrayList;

public class ImageTableOcrNode extends IRpaTaskNode {
    private String outputParamName;
    private RpaTaskOutput output;
    private InputSourceParams inputSource;
    String[] outputAttributeList;

    public ImageTableOcrNode(TaskNodeDetail nodeDetail){
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
                    JSONObject ocrResult = AiEnhanceTool.getAiResult(src, AiEnhanceTool.TABLE_OCR);
                    String html = ocrResult.getString("res");
                    addOutput(getJsonByHTML(html));
                }
                break;
            }
        }
        return output;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {

    }

    private JSONObject getJsonByHTML(String html){
        JSONObject object = new JSONObject();
        while(StringUtils.isNotEmpty(html)){
            JSONObject resByTags = getResByTags(html, "<tr>", "</tr>");
            int end = resByTags.getInteger("end");
            String tds = resByTags.getString("str");
            html = html.substring(end);
            while(StringUtils.isNotEmpty(tds)){
                JSONObject attrJson = getResByTags(tds, "<td>", "</td>");
                end = attrJson.getInteger("end");
                tds = tds.substring(end);
                String attributeStr = attrJson.getString("str");

                if(StringUtils.isEmpty(tds)){
                    break;
                }
                JSONObject valueJson = getResByTags(tds, "<td>", "</td>");
                end = valueJson.getInteger("end");
                tds = tds.substring(end);
                String valueStr = valueJson.getString("str");

                for(String str : outputAttributeList){
                    if(isStrSimilar(attributeStr, str)){
                        object.put(str, valueStr);
                        break;
                    }
                }
            }
        }
        return object;
    }

    private JSONObject getResByTags(String html, String startFlag, String endFlag){
        int start = html.indexOf(startFlag);
        int end = html.indexOf(endFlag);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("str", html.substring(start, end+endFlag.length()));
        jsonObject.put("end", end + endFlag.length());
        return jsonObject;
    }

    private boolean isStrSimilar(String str1, String str2){
        double similar = StrUtil.similar(str1, str2);
        return similar > 0.9;
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
