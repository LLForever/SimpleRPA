package com.simplerpa.cloudservice.entity.util;

import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月29日 21:40
 */

public class RpaTaskStructure {
    // RPA任务邻接表
    private HashMap<String, ArrayList<String> > adjacencyList;

    // RPA节点表
    private HashMap<String, IRpaTaskNode> nodeList;

    // 节点入度表
    private HashMap<String, Integer> inDegreeList;

    // 开始节点的id
    private String startNodeId;

    public RpaTaskStructure(){
        nodeList = new HashMap<>();
        adjacencyList = new HashMap<>();
        startNodeId = null;
    }

    /**
     * 添加一个节点对象，如果已经存在，返回false
     * */
    public Boolean addNode(String name, IRpaTaskNode node){
        if(nodeList.containsKey(name)){
            return false;
        }
        nodeList.put(name, node);
        return true;
    }

    public void addEdge(String origin, String target){
        if(!adjacencyList.containsKey(origin)){
            adjacencyList.put(origin, new ArrayList<>());
        }
        adjacencyList.get(origin).add(target);
    }

    /**
     * 生成拓扑执行序列，如果拓扑排序失败，则返回false
     * */
    public Boolean generateExecuteList(){
        if(startNodeId == null){
            return false;
        }

        return true;
    }

    public HashMap<String, ArrayList<String>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(HashMap<String, ArrayList<String>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public HashMap<String, IRpaTaskNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(HashMap<String, IRpaTaskNode> nodeList) {
        this.nodeList = nodeList;
    }

    public String getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(String startNodeId) {
        this.startNodeId = startNodeId;
    }
}
