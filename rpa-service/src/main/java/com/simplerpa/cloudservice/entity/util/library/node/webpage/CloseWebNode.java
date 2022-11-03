package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

public class CloseWebNode extends IRpaTaskNode {
    private InputSourceParams inputSourceParams;

    public CloseWebNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        String parentSource = inputSourceParams.getParentSource();
        ArrayList<JSONObject> resultByParamName = input.getResultByParamName(parentSource);
        for(JSONObject jsonObject : resultByParamName){
            if(jsonObject.containsKey(DictionaryUtil.HTML_FLAG)){
                WebDriver webDriver = (WebDriver) jsonObject.get(DictionaryUtil.HTML_FLAG);
                webDriver.quit();
                break;
            }
        }
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) throws Exception {

    }

    @Override
    public void clearRpaOutput() {

    }

    public InputSourceParams getInputSourceParams() {
        return inputSourceParams;
    }

    public void setInputSourceParams(InputSourceParams inputSourceParams) {
        this.inputSourceParams = inputSourceParams;
    }
}
