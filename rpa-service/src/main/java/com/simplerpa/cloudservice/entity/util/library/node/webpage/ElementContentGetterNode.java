package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;

public class ElementContentGetterNode extends WebAction {
    private String outputParamName;

    public ElementContentGetterNode(TaskNodeDetail nodeDetail){
        super(nodeDetail);
    }

    @Override
    public RpaTaskOutput operateElement(WebDriver driver, ArrayList<JSONObject> list) {
//        Actions actions = new Actions(driver);
        WebElement element = getElement(driver, list);
        if(element == null){
            return null;
        }
        RpaTaskOutput output = new RpaTaskOutput();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DictionaryUtil.SINGLE_PARAM_FLAG, element.getText());
        output.addObject(getOutputParamName(), jsonObject);
        return output;
    }

    public String getOutputParamName() {
        return outputParamName;
    }

    public void setOutputParamName(String outputParamName) {
        this.outputParamName = outputParamName;
    }
}
