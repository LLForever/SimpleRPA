package com.simplerpa.cloudservice.entity.util.library.node.judge;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

public class SingleConditionNode extends IRpaTaskNode {
    private String inputText, targetText;
    private String judgeType;
    private Object inputObj, targetObj;
    private RpaTaskOutput output;

    public SingleConditionNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        detectParamsValue(input);
        JSONObject jsonObject = new JSONObject();
        if("notequal".equals(judgeType)){
            jsonObject.put(DictionaryUtil.JUDGE_FLAG, NotEqual(inputObj, targetObj));
        }else if("contain".equals(judgeType)){
            jsonObject.put(DictionaryUtil.JUDGE_FLAG, Contain(inputObj, targetObj));
        }else if("notcontain".equals(judgeType)){
            jsonObject.put(DictionaryUtil.JUDGE_FLAG, NotContain(inputObj, targetObj));
        }else if("appear".equals(judgeType)){
            jsonObject.put(DictionaryUtil.JUDGE_FLAG, Appear(inputObj));
        }else if("disappear".equals(judgeType)){
            jsonObject.put(DictionaryUtil.JUDGE_FLAG, Disappear(inputObj));
        }else {
            jsonObject.put(DictionaryUtil.JUDGE_FLAG, Equal(inputObj, targetObj));
        }
        addOutput(jsonObject);
        return output;
    }

    private boolean NotEqual(Object src, Object target){
        if(src == null || target == null){
            return false;
        }
        if(src instanceof JSONObject){
            if(target instanceof JSONObject){
                JSONObject srcJson = (JSONObject) src, targetJson = (JSONObject) target;
                return !srcJson.toString().equals(targetJson.toString());
            }else if(target instanceof ArrayList){
                return NotEqual(src, getSingleParam(target));
            }
        }else if(src instanceof ArrayList){
            if(target instanceof ArrayList){
                return NotEqual(getSingleParam(src), getSingleParam(target));
            }
            return NotEqual(getSingleParam(src), target);
        }
        return !src.equals(target);
    }

    private boolean Equal(Object src, Object target){
        if(src == null || target == null){
            return false;
        }
        if(src instanceof JSONObject){
            if(target instanceof JSONObject){
                JSONObject srcJson = (JSONObject) src, targetJson = (JSONObject) target;
                return srcJson.toString().equals(targetJson.toString());
            }else if(target instanceof ArrayList){
                return Equal(src, getSingleParam(target));
            }
        }else if(src instanceof ArrayList){
            if(target instanceof ArrayList){
                return Equal(getSingleParam(src), getSingleParam(target));
            }
            return Equal(getSingleParam(src), target);
        }
        return src.equals(target);
    }

    private boolean Appear(Object src){
        if(src == null){
            return false;
        }
        if(src instanceof ArrayList) {
            InputSourceParams inputSourceParams = new InputSourceParams(JSON.parseObject(inputText));
            ArrayList resultByParamName = (ArrayList) src;
            if (resultByParamName.get(0) instanceof JSONObject) {
                ArrayList<JSONObject> list = (ArrayList<JSONObject>) src;
                for (JSONObject obj : list) {
                    if (obj.containsKey(DictionaryUtil.HTML_FLAG)) {
                        WebDriver webDriver = (WebDriver) obj.get(DictionaryUtil.HTML_FLAG);
                        try {
                            new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(inputSourceParams.getChildSource())));
                            return true;
                        } catch (Exception e) {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean Disappear(Object src){
        if(src == null){
            return false;
        }
        if(src instanceof ArrayList) {
            InputSourceParams inputSourceParams = new InputSourceParams(JSON.parseObject(inputText));
            ArrayList resultByParamName = (ArrayList) src;
            if (resultByParamName.get(0) instanceof JSONObject) {
                ArrayList<JSONObject> list = (ArrayList<JSONObject>) src;
                for (JSONObject obj : list) {
                    if (obj.containsKey(DictionaryUtil.HTML_FLAG)) {
                        WebDriver webDriver = (WebDriver) obj.get(DictionaryUtil.HTML_FLAG);
                        try {
                            new WebDriverWait(webDriver, 5).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(inputSourceParams.getChildSource())));
                            return true;
                        } catch (Exception e) {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean Contain(Object src, Object target){
        if(src == null || target == null){
            return false;
        }
        if(src instanceof JSONObject){
            if(target instanceof JSONObject){
                JSONObject srcJson = (JSONObject) src, targetJson = (JSONObject) target;
                return srcJson.toString().contains(targetJson.toJSONString());
            }else if(target instanceof ArrayList){
                return Contain(src, getSingleParam(target));
            }
        }else if(src instanceof String){
            if(target instanceof String){
                return ((String) src).contains((String) target);
            }else if(target instanceof ArrayList){
                return Contain(src, getSingleParam(target));
            }
        }else if(src instanceof ArrayList){
            if(target instanceof ArrayList){
                return Contain(getSingleParam(src), getSingleParam(target));
            }
            return Contain(getSingleParam(src), target);
        }
        return false;
    }

    private boolean NotContain(Object src, Object target){
        if(src == null || target == null){
            return false;
        }
        if(src instanceof JSONObject){
            if(target instanceof JSONObject){
                JSONObject srcJson = (JSONObject) src, targetJson = (JSONObject) target;
                return !srcJson.toString().contains(targetJson.toJSONString());
            }else if(target instanceof ArrayList){
                return NotContain(src, getSingleParam(target));
            }
        }else if(src instanceof String){
            if(target instanceof String){
                return !((String) src).contains((String) target);
            }else if(target instanceof ArrayList){
                return NotContain(src, getSingleParam(target));
            }
        }else if(src instanceof ArrayList){
            if(target instanceof ArrayList){
                return NotContain(getSingleParam(src), getSingleParam(target));
            }
            return NotContain(getSingleParam(src), target);
        }
        return false;
    }

    private Object getSingleParam(Object src){
        ArrayList resultByParamName = (ArrayList) src;
        if (resultByParamName.get(0) instanceof JSONObject) {
            ArrayList<JSONObject> list = (ArrayList<JSONObject>) src;
            for (JSONObject obj : list) {
                if(obj.containsKey(DictionaryUtil.SINGLE_PARAM_FLAG)){
                    return obj.get(DictionaryUtil.SINGLE_PARAM_FLAG);
                }
            }
        }
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) throws Exception {
        inputObj = getObjectByParams(inputText, input);
        if(targetText != null && targetText.length() > 0){
            targetObj = getObjectByParams(targetText, input);
        }
        if(inputObj == null){
            inputObj = inputText;
        }
        if(targetObj == null){
            targetObj = targetText;
        }
    }

    @Override
    public void clearRpaOutput() {
        output = new RpaTaskOutput();
    }

    private void addOutput(JSONObject jsonObject){
        if(output == null){
            output = new RpaTaskOutput();
        }
        output.addObjectDistinct(DictionaryUtil.JUDGE_FLAG, jsonObject);
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

    public String getJudgeType() {
        return judgeType;
    }

    public void setJudgeType(String judgeType) {
        this.judgeType = judgeType;
    }

    public Object getInputObj() {
        return inputObj;
    }

    public void setInputObj(Object inputObj) {
        this.inputObj = inputObj;
    }

    public Object getTargetObj() {
        return targetObj;
    }

    public void setTargetObj(Object targetObj) {
        this.targetObj = targetObj;
    }

    public RpaTaskOutput getOutput() {
        return output;
    }

    public void setOutput(RpaTaskOutput output) {
        this.output = output;
    }
}
