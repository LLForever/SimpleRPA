package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WriteInputNode extends WebAction{
    private String inputText;

    public WriteInputNode(TaskNodeDetail taskNodeDetail) {
        super(taskNodeDetail);
    }

    @Override
    public void operateElement(WebDriver driver) {
        driver.findElement(By.xpath(getxPath())).sendKeys(getInputText());
    }

    public String getInputText() {
        return inputText == null ? "" : inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }
}
