package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
        try{
            int cnt = 2;
            while(cnt > 0){
                try {
                    element.click();
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }
                cnt--;
                Thread.sleep(500);
            }
        }catch (Exception e){
            JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
            javascriptExecutor.executeScript("arguments[0].click();", element);
        }
        return null;
    }

    @Override
    public void clearRpaOutput() {

    }
}
