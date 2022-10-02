package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class OpenWebPageNode extends IRpaTaskNode {
    private String URL;
    private String outputParamName;
    private RpaTaskOutput output;

    public OpenWebPageNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(URL == null || outputParamName == null){
            throw new Exception(this.getClass().getName() + " : 缺少必要参数，执行失败！");
        }
        JSONObject jsonObject = new JSONObject();
        WebDriverManager.chromedriver().setup();
        WebDriver webDriver = new ChromeDriver();
        webDriver.get(getURL());
        jsonObject.put(DictionaryUtil.HTML_FLAG, webDriver);
        addOutput(jsonObject);
        return output;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {
        URL = changeStringParams(URL, input);
    }

    private void addOutput(JSONObject jsonObject){
        if(output == null){
            output = new RpaTaskOutput();
        }
        output.addObject(outputParamName, jsonObject);
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getOutputParamName() {
        return outputParamName;
    }

    public void setOutputParamName(String outputParamName) {
        this.outputParamName = outputParamName;
    }
}
