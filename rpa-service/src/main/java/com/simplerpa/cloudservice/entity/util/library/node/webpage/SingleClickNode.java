package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

public class SingleClickNode implements IRpaTaskNode {
    private final TaskNodeDetail taskNodeDetail;

    private InputSourceParams inputSourceParams;
    private String xPath;

    public SingleClickNode(TaskNodeDetail taskNodeDetail){
        this.taskNodeDetail = taskNodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(inputSourceParams == null){
            throw new Exception(this.getClass().getName() + " : 缺少必要参数，执行失败！");
        }
        String parentSource = inputSourceParams.getParentSource();
        if(parentSource != null && xPath != null){
            ArrayList<JSONObject> resultByParamName = input.getResultByParamName(parentSource);
            for(JSONObject jsonObject : resultByParamName){
                if(jsonObject.containsKey(DictionaryUtil.HTML_FLAG)){
                    WebDriver webDriver = (WebDriver) jsonObject.get(DictionaryUtil.HTML_FLAG);
                    webDriver.findElement(By.xpath(xPath)).click();
                    Object[] objects = webDriver.getWindowHandles().toArray();
                    webDriver.switchTo().window((String) objects[objects.length-1]);
//                    System.out.println(webDriver.getTitle());
                    break;
                }
            }
        }
        return null;
    }

    @Override
    public TaskNodeDetail getRpaTaskDetail() {
        return taskNodeDetail;
    }

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
