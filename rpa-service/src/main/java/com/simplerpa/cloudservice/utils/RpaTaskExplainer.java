package com.simplerpa.cloudservice.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskLineDetail;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.entity.util.RpaTaskStructure;
import com.simplerpa.cloudservice.entity.util.library.tools.AiEnhanceTool;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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
            String url = "https://img1.baidu.com/it/u=3704169818,578020351&fm=253&fmt=auto&app=138&f=JPEG?w=640&h=450";

//            String url = "https://s1.ax1x.com/2022/11/02/xHvKfJ.png";
//            String fapiaoUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fss2.meipian.me%2Fusers%2F18784074%2F6ac8639a5f21443493b4aa0685450458.jpg%3Fmeipian-raw%2Fbucket%2Fivwen%2Fkey%2FdXNlcnMvMTg3ODQwNzQvNmFjODYzOWE1ZjIxNDQzNDkzYjRhYTA2ODU0NTA0NTguanBn%2Fsign%2Fe68651a55681b23ccb968ce32f4eb155.jpg&refer=http%3A%2F%2Fss2.meipian.me&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1670052461&t=94ea15524e5ae091b5806866eac8169d";
            String[] strArr = {"姓名", "证号", "电话", "邮箱"};
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
