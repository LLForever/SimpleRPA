package com.simplerpa.cloudservice.entity.util;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月29日 21:50
 */

public class RpaTaskOutput {
    // 运行结果
    private HashMap<String, ArrayList<JSONObject>> output;

    public RpaTaskOutput(){
        output = new HashMap<>();
    }

    /**
     * 合并两个output，若相同则覆盖
     * */
    public void mergeOutput(RpaTaskOutput rpaTaskOutput){
        if(rpaTaskOutput == null){
            return;
        }
        output.putAll(rpaTaskOutput.getOutput());
    }

    /**
     * 合并两个output且只做追加，不做新增和覆盖
    * */
    public void mergeOutputOnlyAdd(RpaTaskOutput rpaTaskOutput){
        if(rpaTaskOutput == null){
            return;
        }
        for (Map.Entry<String, ArrayList<JSONObject>> entry: rpaTaskOutput.getOutput().entrySet()) {
            if(output.containsKey(entry.getKey())){
                output.get(entry.getKey()).addAll(entry.getValue()); // 追加记录
            }
        }
    }

    public ArrayList<JSONObject> getResultByParamName(String str){
        if(!hasParam(str)){
            return null;
        }
        return output.get(str);
    }

    public boolean hasParam(String str){
        return output.containsKey(str);
    }

    /**
     * 添加一个json数组，若已经存在某个key，则返回false
     * */
    private Boolean canAddList(String key, ArrayList<JSONObject> objects){
        if(objects == null){
            return false;
        }
        if(output.containsKey(key)){
            return false;
        }
        output.put(key, objects);
        return true;
    }

    /**
     * 添加一个json对象（允许重复），若新增某个json数组时失败，则返回false
     * */
    public Boolean addObject(String key, JSONObject jsonObject){
        if(jsonObject == null){
            return false;
        }
        if(!output.containsKey(key)){
            if(canAddList(key, new ArrayList<>())){
                output.get(key).add(jsonObject);
                return true;
            }
        }else{
            output.get(key).add(jsonObject);
            return true;
        }
        return false;
    }

    public Boolean addObjectDistinct(String key, JSONObject jsonObject){
        if(jsonObject == null){
            return false;
        }
        if(!output.containsKey(key)){
            if(canAddList(key, new ArrayList<>())){
                output.get(key).add(jsonObject);
                return true;
            }
        }else{
            output.get(key).clear();
            output.get(key).add(jsonObject);
            return true;
        }
        return false;
    }

    public HashMap<String, ArrayList<JSONObject>> getOutput() {
        return output;
    }

    public void setOutput(HashMap<String, ArrayList<JSONObject>> output) {
        this.output = output;
    }
}
