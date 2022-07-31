package com.simplerpa.cloudservice.entity.util.library.node.other;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

import java.util.ArrayList;
import java.util.Date;

public class DateToTimestampNode extends IRpaTaskNode {
    private InputSourceParams inputSource;

    public DateToTimestampNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(inputSource == null){
            throw new Exception(this.getClass().getName() + " : 缺少必要参数，执行失败！");
        }
        String childSource = inputSource.getChildSource();
        String parentSource = inputSource.getParentSource();
        if(parentSource == null){
            throw new Exception(this.getClass().getName() + "缺少输入参数");
        }
        ArrayList<JSONObject> resultByParamName = input.getResultByParamName(parentSource);
        if(childSource == null){
            changeSingleResult(resultByParamName);
        }else{
            changeResult(resultByParamName, childSource);
        }
        return null;
    }

    private void changeSingleResult(ArrayList<JSONObject> result){
        changeResult(result, DictionaryUtil.SINGLE_PARAM_FLAG);
    }

    private void changeResult(ArrayList<JSONObject> result, String target){
        for(JSONObject item : result){
            if(item.containsKey(target)){
                Date date = item.getDate(target);
                item.put(target, date.getTime());
            }
        }
    }

    public InputSourceParams getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSourceParams inputSource) {
        this.inputSource = inputSource;
    }
}
