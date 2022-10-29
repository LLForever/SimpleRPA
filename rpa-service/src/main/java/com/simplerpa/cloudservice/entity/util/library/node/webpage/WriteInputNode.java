package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

public class WriteInputNode extends WebAction{
    private String inputText, inputTextBck;

    public WriteInputNode(TaskNodeDetail taskNodeDetail) {
        super(taskNodeDetail);
    }

    @Override
    public RpaTaskOutput operateElement(WebDriver driver, ArrayList<JSONObject> list) {
        WebElement element = getElement(driver, list);
        element.sendKeys(Keys.CONTROL,"a");
//        element.clear();
        element.sendKeys(getInputText());
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) throws Exception {
        this.setxPath(transformParams(this.getxPath(), this.getxPathBck(), input));
        inputText = transformParams(inputText, inputTextBck, input);
    }

    @Override
    public void clearRpaOutput() {

    }

    public String getInputText() {
        return inputText == null ? "" : inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getInputTextBck() {
        return inputTextBck;
    }

    public void setInputTextBck(String inputTextBck) {
        this.inputTextBck = inputTextBck;
    }
}
