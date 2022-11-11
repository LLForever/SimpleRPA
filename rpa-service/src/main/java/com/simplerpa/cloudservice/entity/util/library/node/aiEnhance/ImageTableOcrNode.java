package com.simplerpa.cloudservice.entity.util.library.node.aiEnhance;

import cn.hutool.core.util.StrUtil;
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

import java.net.URL;
import java.util.ArrayList;

public class ImageTableOcrNode extends IRpaTaskNode {
    private String outputParamName;
    private RpaTaskOutput output;
    private InputSourceParams inputSource;
    String[] outputAttributeList;
    Boolean horizontal;
    ArrayList<ArrayList<String> > tableList;

    public ImageTableOcrNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
        this.tableList = new ArrayList<>();
        horizontal = false;
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
                    getTableListByHTML(html);
                    getAttributeValue();
                }
            }else if(jsonObject.containsKey(DictionaryUtil.SINGLE_PARAM_FLAG)){
                try {
                    String string = jsonObject.getString(DictionaryUtil.SINGLE_PARAM_FLAG);
                    if(RpaTools.isURLValid(string)){
                        JSONObject aiResult = AiEnhanceTool.getAiResult(string, AiEnhanceTool.TABLE_OCR);
                        String html = aiResult.getString("res");
                        getTableListByHTML(html);
                        getAttributeValue();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return output;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {

    }

    @Override
    public void clearRpaOutput() {
        output = new RpaTaskOutput();
    }

    private void getAttributeValue(){
        JSONObject jsonObject = new JSONObject();
        if(horizontal){
            for(ArrayList<String> item : tableList){
                for(int i=0; i<item.size(); i++){
                     for(String str : outputAttributeList){
                         if(StrUtil.similar(str, item.get(i)) >= 0.9 && i != item.size()-1){
                             jsonObject.put(str, item.get(i+1));
                             i++;
                             break;
                         }
                         String s = item.get(i);
                         if(s.length() > str.length()+1){
                             String substring = s.substring(0, str.length());
                             if(StrUtil.similar(substring, str) >= 0.9){
                                 if(s.charAt(str.length()) == ':' || s.charAt(str.length()) == '：' || s.charAt(str.length()) == ' '){
                                     substring = s.substring(str.length()+1);
                                 }else{
                                     substring = s.substring(str.length());
                                 }
                                 jsonObject.put(str, substring);
                             }
                             break;
                         }
                     }
                }
            }
            addOutput(jsonObject);
        }else{
            for(int z=1; z<tableList.size(); z++){
                jsonObject = new JSONObject();
                ArrayList<String> item = tableList.get(z);
                for(int i=0; i<item.size(); i++){
                    if(i >= tableList.get(0).size()){
                        break;
                    }
                    for(String str : outputAttributeList){
                        if(StrUtil.similar(str, tableList.get(0).get(i)) >= 0.9){
                            jsonObject.put(str, item.get(i));
                            break;
                        }
                    }
                }
                addOutput(jsonObject);
            }
        }
    }

    private void getTableListByHTML(String html){
        while(StringUtils.isNotEmpty(html)){
            JSONObject resByTags = getResByTags(html, "<tr>", "</tr>");
            int end = resByTags.getInteger("end");
            if(end == -1){
                break;
            }
            String tds = resByTags.getString("str");
            html = html.substring(end);
            ArrayList<String> arrayList = new ArrayList<>();
            while(StringUtils.isNotEmpty(tds)){
                JSONObject attrJson = getResByTags(tds, "<td>", "</td>");
                end = attrJson.getInteger("end");
                if(end == -1){
                    break;
                }
                tds = tds.substring(end);
                String str = attrJson.getString("str");
                arrayList.add(str);
            }
            tableList.add(arrayList);
        }
    }

    private JSONObject getResByTags(String html, String startFlag, String endFlag){
        int start = html.indexOf(startFlag);
        int end = html.indexOf(endFlag);
        JSONObject jsonObject = new JSONObject();
        if(end == -1 || start == -1){
            jsonObject.put("str", "");
            jsonObject.put("end", -1);
            return jsonObject;
        }
        jsonObject.put("str", html.substring(start + startFlag.length(), end));
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

    public Boolean getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(Boolean horizontal) {
        this.horizontal = horizontal;
    }
}
