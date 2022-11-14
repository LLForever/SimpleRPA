package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

public class WaitElementDisappearNode extends WebAction{
    public WaitElementDisappearNode(TaskNodeDetail nodeDetail){
        super(nodeDetail);
    }

    @Override
    public void clearRpaOutput() {

    }

    @Override
    public RpaTaskOutput operateElement(WebDriver driver, ArrayList<JSONObject> list) {
        try {
            new WebDriverWait(driver, 5).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(getxPath())));
        }catch (Exception e){
            try {
                throw new Exception("[" + nodeDetail.getName() + "]: " +  "元素一直存在，请检查元素是否会消失！");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
