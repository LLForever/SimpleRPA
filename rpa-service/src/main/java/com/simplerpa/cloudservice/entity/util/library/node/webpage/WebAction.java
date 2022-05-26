package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

public abstract class WebAction implements IRpaTaskNode {
    private final TaskNodeDetail taskNodeDetail;

    private InputSourceParams inputSourceParams;
    private String xPath;

    public WebAction(TaskNodeDetail taskNodeDetail){
        this.taskNodeDetail = taskNodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(inputSourceParams == null){
            throw new Exception(this.getClass().getName() + " : 缺少必要参数，执行失败！");
        }
        String parentSource = inputSourceParams.getParentSource();
        if(parentSource != null && xPath != null){
            doAction(input, parentSource);
        }
        return null;
    }

    @Override
    public TaskNodeDetail getRpaTaskDetail() {
        return taskNodeDetail;
    }

    private void doAction(RpaTaskOutput input, String parentSource){
        ArrayList<JSONObject> resultByParamName = input.getResultByParamName(parentSource);
        for(JSONObject jsonObject : resultByParamName){
            if(jsonObject.containsKey(DictionaryUtil.HTML_FLAG)){
                WebDriver webDriver = (WebDriver) jsonObject.get(DictionaryUtil.HTML_FLAG);
                operateElement(webDriver);
                Object[] objects = webDriver.getWindowHandles().toArray();
                webDriver.switchTo().window((String) objects[objects.length-1]);
                break;
            }
        }
    }

    public abstract void operateElement(WebDriver driver);

    public InputSourceParams getInputSourceParams() {
        return inputSourceParams;
    }

    public void setInputSourceParams(InputSourceParams inputSourceParams) {
        this.inputSourceParams = inputSourceParams;
    }

    public String getxPath() {
        return xPath;
    }

    public void setxPath(String xPath) {
        this.xPath = xPath;
    }
}
