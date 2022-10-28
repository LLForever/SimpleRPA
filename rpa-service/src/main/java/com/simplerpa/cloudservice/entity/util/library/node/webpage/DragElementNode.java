package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;

public class DragElementNode extends WebAction{
    private String targetPosition, targetPositionBck;

    public DragElementNode(TaskNodeDetail taskNodeDetail){
        super(taskNodeDetail);
    }

    @Override
    public RpaTaskOutput operateElement(WebDriver driver, ArrayList<JSONObject> list) {
        WebElement source = getElement(driver, list);
        WebElement target = driver.findElement(By.xpath(targetPosition));
        Actions actions = new Actions(driver);
        actions.dragAndDrop(source, target).perform();
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) throws Exception {
        this.setxPath(transformParams(this.getxPath(), this.getxPathBck(), input));
        targetPosition = transformParams(targetPosition, targetPositionBck, input);
    }

    @Override
    public void clearRpaOutput() {

    }

    public String getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(String targetPosition) {
        this.targetPosition = targetPosition;
    }

    public String getTargetPositionBck() {
        return targetPositionBck;
    }

    public void setTargetPositionBck(String targetPositionBck) {
        this.targetPositionBck = targetPositionBck;
    }
}
