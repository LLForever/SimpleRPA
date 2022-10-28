package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

public class SingleClickNode extends WebAction {

    public SingleClickNode(TaskNodeDetail taskNodeDetail){
        super(taskNodeDetail);
    }

    @Override
    public RpaTaskOutput operateElement(WebDriver driver, ArrayList<JSONObject> list) {
        WebElement element = getElement(driver, list);
        element.click();
        return null;
    }

    @Override
    public void clearRpaOutput() {

    }
}
