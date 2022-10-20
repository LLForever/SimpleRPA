package com.simplerpa.cloudservice.entity.util.library.node.loop;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

import java.util.AbstractCollection;
import java.util.ArrayList;

public class ForLoopNode extends IRpaTaskNode {
    String startPos, endPos;
    ArrayList<IRpaTaskNode> forList;
    private static final String LOOP_END = "loop_end";

    public ForLoopNode(TaskNodeDetail detail){
        this.nodeDetail = detail;
        forList = new ArrayList<>();
    }

    @Override
    public RpaTaskOutput run(RpaTaskOutput input) throws Exception {
        if(forList.isEmpty()){
            return null;
        }
        detectParamsValue(input);
        int i = Integer.parseInt(startPos), end = Integer.parseInt(endPos);
        for(; i<end; i++){
            for (IRpaTaskNode node : forList) {
                RpaTaskOutput run = node.run(input);
                input.mergeOutput(run);
            }
        }
        return null;
    }

    @Override
    public void detectParamsValue(RpaTaskOutput input) {
        Object objectByParams = getObjectByParams(startPos, input);
        if(objectByParams != null){
            startPos = getLengthByObject(objectByParams);
        }
        objectByParams = getObjectByParams(endPos, input);
        if(objectByParams != null){
            endPos = getLengthByObject(objectByParams);
        }
    }

    private String getLengthByObject(Object objectByParams){
        if(objectByParams instanceof String){
            return String.valueOf( ((String) objectByParams).length() );
        }else if(objectByParams instanceof AbstractCollection){
            return String.valueOf( ((AbstractCollection<?>) objectByParams).size() );
        }else if(objectByParams instanceof JSONObject){
            return String.valueOf(((JSONObject) objectByParams).size());
        }
        return "0";
    }

    public boolean addNode(IRpaTaskNode node){
        if(LOOP_END.equals(node.getRpaTaskDetail().getType())){
            return false;
        }
        forList.add(node);
        return true;
    }

    public String getStartPos() {
        return startPos;
    }

    public void setStartPos(String startPos) {
        this.startPos = startPos;
    }

    public String getEndPos() {
        return endPos;
    }

    public void setEndPos(String endPos) {
        this.endPos = endPos;
    }
}
