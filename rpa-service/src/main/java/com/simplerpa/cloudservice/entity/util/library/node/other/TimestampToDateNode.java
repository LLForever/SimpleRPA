package com.simplerpa.cloudservice.entity.util.library.node.other;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

import java.util.ArrayList;
import java.util.Date;

public class TimestampToDateNode extends IRpaTaskNode {
    private InputSourceParams inputSource;

    public TimestampToDateNode(TaskNodeDetail nodeDetail){
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
        if(StringUtils.isNotEmpty(childSource)){
            changeResult(resultByParamName, childSource);
        }else{
            changeSingleResult(resultByParamName);
        }
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {

    }

    private void changeSingleResult(ArrayList<JSONObject> result){
        changeResult(result, DictionaryUtil.SINGLE_PARAM_FLAG);
    }

    private void changeResult(ArrayList<JSONObject> result, String target){
        for(JSONObject item : result){
            if(item.containsKey(target)){
                Long timeStamp = item.getLong(target);
                Date date = new Date(timeStamp);
                item.put(target, date);
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
