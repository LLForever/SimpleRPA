package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class DoubleClickNode extends WebAction {

    public DoubleClickNode(TaskNodeDetail taskNodeDetail){
        super(taskNodeDetail);
    }

    @Override
    public void operateElement(WebDriver driver) {
        Actions actions = new Actions(driver);
        actions.doubleClick(driver.findElement(By.xpath(getxPath()))).perform();
    }
}
