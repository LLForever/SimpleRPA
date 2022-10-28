package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.HashMap;

public class GetElementNode extends WebAction{
    private String outputParamName;

    public GetElementNode(TaskNodeDetail taskNodeDetail) {
        super(taskNodeDetail);
    }

    @Override
    public RpaTaskOutput operateElement(WebDriver driver, ArrayList<JSONObject> list){
        RpaTaskOutput output = new RpaTaskOutput();
        HashMap<String, ArrayList<JSONObject>> hashMap = new HashMap<>();
        JSONObject jsonObject = new JSONObject();

        // 将获取到的元素设置到jsonObject中
        jsonObject.put(outputParamName, getElement(driver, list));
        // 将jsonObject加入到原有的list中
        list.add(jsonObject);
        // 将list加入到结果中
        hashMap.put(getInputSourceParams().getParentSource(), list);
        output.setOutput(hashMap);
        return output;
    }

    public String getOutputParamName() {
        return outputParamName;
    }

    public void setOutputParamName(String outputParamName) {
        this.outputParamName = outputParamName;
    }

    @Override
    public void clearRpaOutput() {

    }
}
