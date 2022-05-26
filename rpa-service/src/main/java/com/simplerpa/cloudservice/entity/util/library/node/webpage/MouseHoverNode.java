package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class MouseHoverNode extends WebAction{

    public MouseHoverNode(TaskNodeDetail taskNodeDetail) {
        super(taskNodeDetail);
    }

    @Override
    public void operateElement(WebDriver driver) {
        Actions act = new Actions(driver);
        act.moveToElement(driver.findElement(By.xpath(getxPath()))).perform();
    }
}
