package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

public class JumpWebNode extends WebAction{
    private String URL, URLBck;

    public JumpWebNode(TaskNodeDetail taskNodeDetail) {
        super(taskNodeDetail);
    }

    @Override
    public RpaTaskOutput operateElement(WebDriver driver, ArrayList<JSONObject> list) {
        driver.navigate().to(URL);
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) throws Exception {
        this.setxPath(transformParams(this.getxPath(), this.getxPathBck(), input));
        URL = transformParams(URL, URLBck, input);
    }

    @Override
    public void clearRpaOutput() {

    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURLBck() {
        return URLBck;
    }

    public void setURLBck(String URLBck) {
        this.URLBck = URLBck;
    }
}
