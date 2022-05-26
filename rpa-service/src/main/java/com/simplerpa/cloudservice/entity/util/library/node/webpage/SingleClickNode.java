package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SingleClickNode extends WebAction {

    public SingleClickNode(TaskNodeDetail taskNodeDetail){
        super(taskNodeDetail);
    }

    @Override
    public void operateElement(WebDriver driver) {
        driver.findElement(By.xpath(getxPath())).click();
    }
}
