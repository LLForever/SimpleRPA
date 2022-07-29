package com.simplerpa.cloudservice.entity.util;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

    // 节点执行序列
    private ArrayList<String> executeList;

    private ArrayList<String> globalQueue;
    private String nowExecuteNodeType;
    private HashSet<String> specialNodeType;

    public RpaTaskStructure(){
        nodeList = new HashMap<>();
        adjacencyList = new HashMap<>();
        inDegreeList = new HashMap<>();
        startNodeId = null;
        executeList = null;
        globalQueue = new ArrayList<>();
        nowExecuteNodeType = null;
        specialNodeType = new HashSet<>();
        specialNodeType.add("for_loop");
        specialNodeType.add("while_loop");
        specialNodeType.add("foreach_loop");
        specialNodeType.add("single_condition");
        specialNodeType.add("multi_condition");
    }

    /**
     * 添加一个节点对象，如果已经存在，返回false
     * */
    public Boolean addNode(TaskNodeDetail detail, IRpaTaskNode node){
        String id = detail.getId();
        if(nodeList.containsKey(id)){
            return false;
        }
        nodeList.put(id, node);
        if("start".equals(detail.getType())){
            setStartNodeId(id);
        }
        return true;
    }

    public void addEdge(String origin, String target){
        if(!adjacencyList.containsKey(origin)){
            adjacencyList.put(origin, new ArrayList<>());
        }
        adjacencyList.get(origin).add(target);

        if(!inDegreeList.containsKey(target)){
            inDegreeList.put(target, 0);
        }
        Integer integer = inDegreeList.get(target);
        inDegreeList.put(target, integer+1);
    }

    public String getNextNodeId(){
        if(startNodeId == null){
            return "";
        }
        if(nowExecuteNodeType == null){
            globalQueue.add(startNodeId);
        }
        String node = "";
        if(!globalQueue.isEmpty()){
            node = globalQueue.remove(0);
            TaskNodeDetail tempDetail = findRpaTaskNode(node).getRpaTaskDetail();
            if(nowExecuteNodeType == null && tempDetail == null){
                nowExecuteNodeType = "start";
            }else if(nowExecuteNodeType != null && tempDetail == null){
                nowExecuteNodeType = "end";
            }else{
                nowExecuteNodeType = tempDetail.getType();
            }
            if(adjacencyList.containsKey(node)){
                for(String nextNodeId : adjacencyList.get(node)){
                    // 将邻接节点的入度减1
                    inDegreeList.put(nextNodeId, inDegreeList.get(nextNodeId)-1);
                    // 如果邻接节点的入度为0，则将其添加到队列中
                    if(inDegreeList.get(nextNodeId) == 0) {
                        if (!specialNodeType.contains(nowExecuteNodeType)) {
                            globalQueue.add(nextNodeId);
                        }
                    }
                }
            }
            if(specialNodeType.contains(nowExecuteNodeType)){
                RpaTaskOutput temp = new RpaTaskOutput();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("RpaTaskStructure", this);
                temp.addObject(DictionaryUtil.GET_NEXT_NODE_FLAG, jsonObject);
                IRpaTaskNode rpaTaskNode = findRpaTaskNode(node);
                try {
                    rpaTaskNode.run(temp);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return node;
    }

    public String globalQueueTop(){
        if(isGlobalQueueEmpty()){
            return null;
        }
        return globalQueue.get(0);
    }

    public Boolean isGlobalQueueEmpty(){
        return globalQueue.isEmpty();
    }

    public Boolean isEnd(){
        return "end".equals(nowExecuteNodeType);
    }

    /**
     * 生成拓扑执行序列，如果拓扑排序失败，则返回false
     * */
    public Boolean generateExecuteList(){
        if(startNodeId == null){
            return false;
        }
        if(executeList != null){
            return true;
        }
        // 备份入度表
        HashMap<String, Integer> inDegreeListBackup = new HashMap<>(inDegreeList);

        // 根据startNodeId、inDegreeList和adjacencyList进行拓扑排序
        // 拓扑结果存储在executeList中
        executeList = new ArrayList<>();
        ArrayList<String> queue = new ArrayList<>();
        queue.add(startNodeId);
        while(!queue.isEmpty()){
            String nodeId = queue.remove(0);
            executeList.add(nodeId);
            if(adjacencyList.containsKey(nodeId)){
                for(String nextNodeId : adjacencyList.get(nodeId)){
                    // 将邻接节点的入度减1
                    inDegreeList.put(nextNodeId, inDegreeList.get(nextNodeId)-1);
                    // 如果邻接节点的入度为0，则将其添加到队列中
                    if(inDegreeList.get(nextNodeId) == 0){
                        // 将邻接节点添加到队列中
                        queue.add(nextNodeId);
                        // 将邻接节点从入度表中删除
                        inDegreeList.remove(nextNodeId);
                    }
                }
            }
        }

        // 如果拓扑排序失败，则返回false
        if(executeList.size() != nodeList.size()){
            // 恢复入度表
            inDegreeList.clear();
            inDegreeList.putAll(inDegreeListBackup);

            executeList = null;
            return false;
        }
        return true;
    }

    public IRpaTaskNode findRpaTaskNode(String key){
        if(nodeList.containsKey(key)){
            return nodeList.get(key);
        }
        return null;
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

    public ArrayList<String> getExecuteList() {
        return executeList;
    }

    public void setExecuteList(ArrayList<String> executeList) {
        this.executeList = executeList;
    }

    public HashMap<String, Integer> getInDegreeList() {
        return inDegreeList;
    }

    public void setInDegreeList(HashMap<String, Integer> inDegreeList) {
        this.inDegreeList = inDegreeList;
    }
}
