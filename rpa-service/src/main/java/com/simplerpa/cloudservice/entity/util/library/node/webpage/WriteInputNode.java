package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

public class WriteInputNode extends WebAction{
    private String inputText;

    public WriteInputNode(TaskNodeDetail taskNodeDetail) {
        super(taskNodeDetail);
    }

    @Override
    public RpaTaskOutput operateElement(WebDriver driver, ArrayList<JSONObject> list) {
        WebElement element = getElement(driver, list);
        element.sendKeys(getInputText());
        return null;
    }

    public String getInputText() {
        return inputText == null ? "" : inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }
}
