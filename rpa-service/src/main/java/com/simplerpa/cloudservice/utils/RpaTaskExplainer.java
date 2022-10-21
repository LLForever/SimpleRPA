package com.simplerpa.cloudservice.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.TaskLineDetail;
import com.simplerpa.cloudservice.entity.TaskNodeDetail;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.entity.util.RpaTaskStructure;
import com.simplerpa.cloudservice.entity.util.library.tools.AiEnhanceTool;
import com.simplerpa.cloudservice.utils.factory.RpaNodeFactory;
import io.github.bonigarcia.wdm.WebDriverManager;

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
            RpaNodeFactory factory = RpaNodeFactory.getFactory(nodeDetail);
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
        return rpaTaskStructure;
    }
//
    public static void main(String[] args) {
        try {
            String url = "https://ai.bdstatic.com/file/5DA8385D9D9F42CE866DB9738F68F7DB";
            JSONObject ocrResult = AiEnhanceTool.getAiResult(url, "TABLE_OCR");
            System.out.println(JSONObject.toJSONString(ocrResult));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
