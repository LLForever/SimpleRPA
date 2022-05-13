package com.simplerpa.cloudservice.utils;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.PanelTaskMessage;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.RpaTaskStructure;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.websocket.WebsocketTask;

import java.util.ArrayList;

public class RpaTaskExecutor implements Runnable{
    private final TaskDetailVO taskDetailVO;

    public RpaTaskExecutor(TaskDetailVO taskDetailVO) {
        this.taskDetailVO = taskDetailVO;
    }

    @Override
    public void run() {
        RpaTaskStructure rpaTaskStructure = explainTask();
        if(rpaTaskStructure != null) {
            ArrayList<String> executeList = rpaTaskStructure.getExecuteList();
            RpaTaskOutput allOutput = new RpaTaskOutput();
            for (int i = 0; i<executeList.size(); i++) {
                IRpaTaskNode rpaTaskNode = rpaTaskStructure.findRpaTaskNode(executeList.get(i));
                try{
                    RpaTaskOutput res = rpaTaskNode.run(allOutput);
                    allOutput.mergeOutput(res);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("now", executeList.get(i));
                    jsonObject.put("next", i == executeList.size()-1? null : executeList.get(i+1));
                    PanelTaskMessage panelTaskMessage =
                            new PanelTaskMessage(DictionaryUtil.TASK_MESSAGE_OK, jsonObject);
                    WebsocketTask.notifyObserver(taskDetailVO.getTaskId(), panelTaskMessage);
                    Thread.sleep(100);
                }catch (Exception e){
                    PanelTaskMessage panelTaskMessage =
                            new PanelTaskMessage(DictionaryUtil.TASK_MESSAGE_RUN_ERROR, e.getMessage());
                    WebsocketTask.sendMessageToUser(taskDetailVO.getTaskId(), taskDetailVO.getUserId(), panelTaskMessage);
                    return;
                }
            }
        }

    }

    private RpaTaskStructure explainTask(){
        try{
            return RpaTaskExplainer.explain(taskDetailVO);
        }catch (Exception e){
            PanelTaskMessage panelTaskMessage =
                    new PanelTaskMessage(DictionaryUtil.TASK_MESSAGE_EXPLAIN_ERROR, e.getMessage());
            WebsocketTask.sendMessageToUser(taskDetailVO.getTaskId(), taskDetailVO.getUserId(), panelTaskMessage);
        }
        return null;
    }
}