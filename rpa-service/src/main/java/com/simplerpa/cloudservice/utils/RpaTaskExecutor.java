package com.simplerpa.cloudservice.utils;

import com.alibaba.fastjson.JSONObject;
import com.simplerpa.cloudservice.entity.PanelTaskMessage;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
import com.simplerpa.cloudservice.entity.util.RpaTaskOutput;
import com.simplerpa.cloudservice.entity.util.RpaTaskStructure;
import com.simplerpa.cloudservice.entity.util.base.IRpaTaskNode;
import com.simplerpa.cloudservice.entity.util.library.node.loop.ForLoopNode;
import com.simplerpa.cloudservice.websocket.WebsocketTask;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;

public class RpaTaskExecutor implements Runnable{

    private final TaskDetailVO taskDetailVO;

    public RpaTaskExecutor(TaskDetailVO taskDetailVO) {
        this.taskDetailVO = taskDetailVO;
    }

    @Override
    @Transactional
    public void run() {
        RpaTaskStructure rpaTaskStructure = explainTask();
        if(rpaTaskStructure != null) {
            TaskCostCountUtil.addCost(TaskCostCountUtil.getSumCostById(taskDetailVO.getId()));
            RpaTaskOutput allOutput = new RpaTaskOutput(true);
            WebsocketTask.getTaskDetailService().changeRpaTaskStatus(DictionaryUtil.TASK_STATUS_RUNNING, taskDetailVO.getTaskId(), taskDetailVO.getUserId());
            while(!rpaTaskStructure.isEnd()){
                String nextNode = rpaTaskStructure.getNextNodeId();
                IRpaTaskNode rpaTaskNode = rpaTaskStructure.findRpaTaskNode(nextNode);

                if("for_loop".equals(rpaTaskNode.getRpaTaskDetail().getType())){
                    ForLoopNode forLoopNode = (ForLoopNode) rpaTaskNode;
                    while(true){
                        String nextNodeId = rpaTaskStructure.getNextNodeId();
                        IRpaTaskNode n = rpaTaskStructure.findRpaTaskNode(nextNodeId);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("now", nextNodeId);
                        jsonObject.put("next", rpaTaskStructure.globalQueueTop());
                        PanelTaskMessage panelTaskMessage =
                                new PanelTaskMessage(DictionaryUtil.TASK_MESSAGE_OK, jsonObject);
                        WebsocketTask.notifyObserver(taskDetailVO.getTaskId(), panelTaskMessage);

                        if(!forLoopNode.addNode(n)){
                            break;
                        }
                    }
                }

                try{
                    RpaTaskOutput res = rpaTaskNode.run(allOutput);
                    if(res == null || !res.hasParam(DictionaryUtil.NO_MERGE_FLAG)){
                        allOutput.mergeOutput(res);
                    }else{
                        updateScreenShotFile(res, rpaTaskStructure.getTaskId());
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("now", nextNode);
                    jsonObject.put("next", rpaTaskStructure.globalQueueTop());
                    PanelTaskMessage panelTaskMessage =
                            new PanelTaskMessage(DictionaryUtil.TASK_MESSAGE_OK, jsonObject);
                    WebsocketTask.notifyObserver(taskDetailVO.getTaskId(), panelTaskMessage);
//                    Thread.sleep(10);
                }catch (Exception e){
                    e.printStackTrace();
                    WebsocketTask.getTaskDetailService().changeRpaTaskStatus(DictionaryUtil.TASK_STATUS_ERROR, taskDetailVO.getTaskId(), taskDetailVO.getUserId());
                    PanelTaskMessage panelTaskMessage =
                            new PanelTaskMessage(DictionaryUtil.TASK_MESSAGE_RUN_ERROR, e.getMessage());
                    WebsocketTask.sendMessageToUser(taskDetailVO.getTaskId(), taskDetailVO.getUserId(), panelTaskMessage);
                    clearSelenium(allOutput);
                    return;
                }
                if(rpaTaskStructure.isGlobalQueueEmpty()){
                    break;
                }
            }
            clearSelenium(allOutput);
            WebsocketTask.getTaskDetailService().changeRpaTaskStatus(DictionaryUtil.TASK_STATUS_COMPLETED, taskDetailVO.getTaskId(), taskDetailVO.getUserId());
        }
    }

    public static void updateScreenShotFile(RpaTaskOutput res, Long taskId){
        ArrayList<JSONObject> resultByParamName = res.getResultByParamName(DictionaryUtil.NO_MERGE_FLAG);
        for(JSONObject object : resultByParamName){
            if(object.containsKey(DictionaryUtil.SINGLE_PARAM_FLAG)){
                JSONObject info = (JSONObject) object.get(DictionaryUtil.SINGLE_PARAM_FLAG);
                byte[] img64 = info.getBytes("img64");
                String nodeId = info.getString("id");
                WebsocketTask.getTaskDetailService().saveImageInfo(taskId, nodeId, img64);
                break;
            }
        }
    }

    private void clearSelenium(RpaTaskOutput rpaTaskOutput){
        TaskCostCountUtil.minusCost(TaskCostCountUtil.getSumCostById(taskDetailVO.getId()));
        if(rpaTaskOutput == null){
            return;
        }
        for (Map.Entry<String, ArrayList<JSONObject>> entry: rpaTaskOutput.getOutput().entrySet()) {
            ArrayList<JSONObject> value = entry.getValue();
            for(JSONObject item : value){
                if(item.containsKey(DictionaryUtil.HTML_FLAG)){
                    WebDriver webDriver = (WebDriver) item.get(DictionaryUtil.HTML_FLAG);
                    webDriver.quit();
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
