package com.simplerpa.cloudservice.entity.util.library.node.webpage;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

public abstract class WebAction extends IRpaTaskNode {
    private InputSourceParams inputSourceParams;
    private String xPath, xPathBck;

    public WebAction(TaskNodeDetail taskNodeDetail){
        this.nodeDetail = taskNodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(inputSourceParams == null){
            throw new Exception(this.getClass().getName() + " : 缺少必要参数，执行失败！");
        }
        detectParamsValue(input);
        RpaTaskOutput output = null;
        String parentSource = inputSourceParams.getParentSource();
        if(parentSource != null){
            output = doAction(input, parentSource);
        }
        return output;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) throws Exception {
        xPath = transformParams(this.getxPath(), this.getxPathBck(), input);
    }

    private RpaTaskOutput doAction(RpaTaskOutput input, String parentSource){
        ArrayList<JSONObject> resultByParamName = input.getResultByParamName(parentSource);
        RpaTaskOutput output = null;
        for(JSONObject jsonObject : resultByParamName){
            if(jsonObject.containsKey(DictionaryUtil.HTML_FLAG)){
                WebDriver webDriver = (WebDriver) jsonObject.get(DictionaryUtil.HTML_FLAG);
                output = operateElement(webDriver, resultByParamName);
                Object[] objects = webDriver.getWindowHandles().toArray();
                webDriver.switchTo().window((String) objects[objects.length-1]);
                break;
            }
        }
        return output;
    }

    public WebElement getElement(WebDriver webDriver, ArrayList<JSONObject> list){
        if(StringUtils.isNotEmpty(xPath)){
            int cnt = 3;
            do{
                try{
                    new WebDriverWait(webDriver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xPath)));
                    new WebDriverWait(webDriver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
                    new WebDriverWait(webDriver, 30).until(ExpectedConditions.elementToBeClickable(By.xpath(xPath)));
                }catch (Exception e){
                    if(cnt <= 1){
                        try {
                            throw new Exception("没有找到相关的元素！");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                cnt--;
            }while (cnt > 0);
            return webDriver.findElement(By.xpath(xPath));
        }else if(StringUtils.isNotEmpty(inputSourceParams.getChildSource())){
            for (JSONObject jsonObject : list) {
                if(jsonObject.containsKey(inputSourceParams.getChildSource())){
                    return (WebElement) jsonObject.get(inputSourceParams.getChildSource());
                }
            }
        }
        return null;
    }

    public abstract RpaTaskOutput operateElement(WebDriver driver, ArrayList<JSONObject> list);

    public InputSourceParams getInputSourceParams() {
        return inputSourceParams;
    }

    public void setInputSourceParams(InputSourceParams inputSourceParams) {
        this.inputSourceParams = inputSourceParams;
    }

    public String getxPath() {
        return xPath;
    }

    public void setxPath(String xPath) {
        this.xPath = xPath;
    }

    public String getxPathBck() {
        return xPathBck;
    }

    public void setxPathBck(String xPathBck) {
        this.xPathBck = xPathBck;
    }
}
