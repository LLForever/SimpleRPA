package com.simplerpa.cloudservice.entity.util;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.util.library.node.ReadTxtNode;

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

    public void mergeOutput(RpaTaskOutput rpaTaskOutput){
        for (Map.Entry<String, ArrayList<JSONObject>> entry: rpaTaskOutput.getOutput().entrySet()) {
            if(output.containsKey(entry.getKey())){
                output.get(entry.getKey()).addAll(entry.getValue()); // 追加记录
            }else{
                output.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public String getTxtValue(String key){
        if(output.containsKey(key)){
            JSONObject jsonObject = output.get(key).get(0);
            if(jsonObject.containsKey(ReadTxtNode.getTxtJsonFlag())){
                return jsonObject.getString(ReadTxtNode.getTxtJsonFlag());
            }
        }
        return null;
    }

    /**
     * 添加一个json数组，若已经存在某个key，则返回false
     * */
    public Boolean addElement(String name, ArrayList<JSONObject> objects){
        if(output.containsKey(name)){
            return false;
        }
        output.put(name, objects);
        return true;
    }

    /**
     * 添加一个json对象，若新增某个json数组时失败，则返回false
     * */
    public Boolean addObject(String name, JSONObject jsonObject){
        if(!output.containsKey(name)){
            if(addElement(name, new ArrayList<>())){
                output.get(name).add(jsonObject);
                return true;
            }
        }else{
            output.get(name).add(jsonObject);
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