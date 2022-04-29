package com.simplerpa.cloudservice.entity.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月29日 21:50
 */

public class RpaTaskOutput {
    // 运行结果
    private HashMap<String, Object> output;

    public RpaTaskOutput(){
        output = new HashMap<>();
    }

    public Boolean addElement(String name, Object o){
        if(output.containsKey(name)){
            return false;
        }
        output.put(name, o);
        return true;
    }

    public HashMap<String, Object> getOutput() {
        return output;
    }

    public void setOutput(HashMap<String, Object> output) {
        this.output = output;
    }
}
