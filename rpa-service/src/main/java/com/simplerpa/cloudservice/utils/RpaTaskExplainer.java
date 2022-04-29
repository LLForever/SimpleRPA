package com.simplerpa.cloudservice.utils;

import com.simplerpa.cloudservice.entity.TaskLineDetail;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.entity.util.RpaTaskStructure;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月29日 21:39
 */

public class RpaTaskExplainer {
    public static RpaTaskStructure explain(TaskDetailVO detail){
        RpaTaskStructure rpaTaskStructure = new RpaTaskStructure();
        for(TaskLineDetail item : detail.getLineList()){
            rpaTaskStructure.addEdge(item.getFrom(), item.getTo());
        }
        return rpaTaskStructure;
    }
}
