package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.PanelTaskMessage;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.websocket.WebsocketTask;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

public class GetScreenshotNode extends WebAction{

    public GetScreenshotNode(TaskNodeDetail nodeDetail){
        super(nodeDetail);
    }

    @Override
    public RpaTaskOutput operateElement(WebDriver driver, ArrayList<JSONObject> list) {
//        String screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        byte[] screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        RpaTaskOutput output = new RpaTaskOutput();
        JSONObject jsonObject = new JSONObject(), content = new JSONObject();
        content.put("img64", screenshotAs);
        content.put("id", nodeDetail.getId());
        jsonObject.put(DictionaryUtil.SINGLE_PARAM_FLAG, content);
        output.addObject(DictionaryUtil.NO_MERGE_FLAG, jsonObject);
        return output;
    }

    @Override
    public void clearRpaOutput() {

    }
}
