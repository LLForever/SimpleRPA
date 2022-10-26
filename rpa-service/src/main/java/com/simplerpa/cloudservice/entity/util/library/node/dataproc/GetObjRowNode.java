package com.simplerpa.cloudservice.entity.util.library.node.dataproc;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.InputSourceParams;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

import java.util.*;

public class GetObjRowNode extends IRpaTaskNode {
    private String outputParamName, rowNumStr;
    private RpaTaskOutput output;
    private InputSourceParams inputSource;
    private Integer rowNum;

    public GetObjRowNode(TaskNodeDetail nodeDetail){
        this.nodeDetail = nodeDetail;
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        String childSource = inputSource.getChildSource();
        String parentSource = inputSource.getParentSource();
        Object objectByParams = getObjectByParams(rowNumStr, input);
        if(objectByParams == null){
            throw new Exception(this.getClass().getName() + "缺少指定行参数");
        }
        rowNum = (Integer) objectByParams;
        if(parentSource == null){
            throw new Exception(this.getClass().getName() + "缺少输入参数");
        }
        ArrayList<JSONObject> resultByParamName = input.getResultByParamName(parentSource);
        if(StringUtils.isEmpty(childSource)){
            if(resultByParamName.size() < rowNum){
                throw new Exception(this.getClass().getName() + " " +
                        getRpaTaskDetail().getName() + "【组件】输入的行数大于目标数组元素个数，请重新输入一个值");
            }else{
                addJsonObject(resultByParamName.get(rowNum-1));
                return output;
            }
        }else{
            Object object = getObject(resultByParamName, childSource);
            if(object == null){
                return null;
            }
            if(object.getClass().isArray()){
                Object[] list = (Object[]) object;
                if(list.length < rowNum){
                    throw new Exception(this.getClass().getName() + " " +
                            getRpaTaskDetail().getName() + "【组件】输入的行数大于目标数组元素个数，请重新输入一个值");
                }else{
                    addJsonObject(list[rowNum-1]);
                    return output;
                }
            }else if(object instanceof AbstractList){
                List list = (List) object;
                if(list.size() < rowNum){
                    throw new Exception(this.getClass().getName() + " " +
                            getRpaTaskDetail().getName() + "【组件】输入的行数大于目标数组元素个数，请重新输入一个值");
                }else{
                    addJsonObject(list.get(rowNum-1));
                    return output;
                }
            }
            return null;
        }
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {

    }

    private void addJsonObject(Object obj){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DictionaryUtil.SINGLE_PARAM_FLAG, obj);
        addOutput(jsonObject);
    }

    private Object getObject(ArrayList<JSONObject> result, String target){
        for(JSONObject item : result) {
            if (item.containsKey(target)) {
                return item.get(target);
            }
        }
        return null;
    }

    private void addOutput(JSONObject jsonObject){
        if(output == null){
            output = new RpaTaskOutput();
        }
        output.addObject(outputParamName, jsonObject);
    }

    public String getOutputParamName() {
        return outputParamName;
    }

    public void setOutputParamName(String outputParamName) {
        this.outputParamName = outputParamName;
    }

    public InputSourceParams getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSourceParams inputSource) {
        this.inputSource = inputSource;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getRowNumStr() {
        return rowNumStr;
    }

    public void setRowNumStr(String rowNumStr) {
        this.rowNumStr = rowNumStr;
    }
}
