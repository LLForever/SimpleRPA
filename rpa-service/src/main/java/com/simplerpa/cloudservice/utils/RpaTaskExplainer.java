package com.simplerpa.cloudservice.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskLineDetail;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.entity.util.RpaTaskStructure;
import com.simplerpa.cloudservice.entity.util.library.tools.AiEnhanceTool;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月29日 21:39
 */

public class RpaTaskExplainer {
    public static RpaTaskStructure explain(TaskDetailVO detail) throws Exception {
        RpaTaskStructure rpaTaskStructure = new RpaTaskStructure();
        for(TaskLineDetail item : detail.getLineList()){
            rpaTaskStructure.addEdge(item.getFrom(), item.getTo());
        }
        for(TaskNodeDetail nodeDetail : detail.getNodeList()){
            RpaNodeFactory factory = RpaNodeFactory.getFactory(nodeDetail, detail.getTaskId());
            if(factory == null){
                throw new Exception("RpaTaskExplainer : 节点[" + nodeDetail.getName() + "],类型[" + nodeDetail.getType() + "]暂时无法解析");
            }
            if(!rpaTaskStructure.addNode(nodeDetail, factory.getInstance())){
                throw new Exception("RpaTaskExplainer : 存在相同的节点ID，无法成功解析！");
            }
        }
//        if(!rpaTaskStructure.generateExecuteList()){
//            throw new Exception("RpaTaskExplainer : 无法完成拓扑排序，请检查是否存在环！");
//        }
        rpaTaskStructure.setTaskId(detail.getTaskId());
        return rpaTaskStructure;
    }
//
    public static void main(String[] args) {
        try {
//            String url = "https://s1.ax1x.com/2022/11/01/x71uWj.png";
            String url = "https://s1.ax1x.com/2022/11/02/xHvKfJ.png";
            String[] strArr = {"姓名", "出生", "发票", "产品", "付款条款"};
            List<String> list = Arrays.asList(strArr);
            JSONObject ocrResult = AiEnhanceTool.getAiResult(url, AiEnhanceTool.KEY_EXT, list);
            System.out.println(JSONObject.toJSONString(ocrResult));
            getAttributeValue(ocrResult.getJSONArray("res"));
            outputRes(ocrResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void outputRes(JSONObject ocrResult){
        ArrayList<String> list = new ArrayList<>();
        JSONArray res = ocrResult.getJSONArray("res");
        for(int i=0; i<res.size(); i++){
            JSONObject object = res.getObject(i, JSONObject.class);
            list.add(object.getString("text"));
        }
        System.out.println(Arrays.toString(list.toArray()));
    }

    private static void getAttributeValue(JSONArray jsonArray){
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        for (String key : jsonObject.keySet()){
            JSONArray jsonArray1 = jsonObject.getJSONArray(key);
            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
            JSONObject o = new JSONObject();
            o.put(key, jsonObject1.getString("text"));
            System.out.println(o);
        }
    }
}
