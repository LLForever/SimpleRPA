package com.simplerpa.cloudservice.websocket;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.PanelTaskMessage;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.service.ITaskDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月21日 21:35
 */

@ServerEndpoint("/task/websocket/{taskJsonInfo}")
@Component
public class WebsocketTask implements IWebsocketTaskSubject {
    private static ITaskDetailService taskDetailService;

    private static Integer onlineUserNumber = 0;
    private static ConcurrentHashMap<Long, TaskDetailVO> taskDetailMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Long, List<IWebsocketTaskObserver> > observerListMap = new ConcurrentHashMap<>();

    private Long clientTaskId, clientUserId;

    @Autowired
    public void setTaskDetailService(ITaskDetailService service){
        taskDetailService = service;
    }

    @OnOpen
    public void clientConnect(Session session, @PathParam("taskJsonInfo") String taskJsonInfo){
        JSONObject jsonObject = (JSONObject) JSONObject.parse("{" + taskJsonInfo + "}");
        Long taskId = jsonObject.getLong("taskId");
        Long userId = jsonObject.getLong("userId");
        if(userId != null){
            clientTaskId = taskId;
            clientUserId = userId;
            if(!observerListMap.containsKey(userId)){
                addOnlineUserNumber();
            }
            WebsocketTaskClient client = new WebsocketTaskClient(session, userId);
            registerObserver(taskId, client);
            try{
                System.out.println("Client Connect Successfully!");
                client.sendMessage(new PanelTaskMessage());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void clientQuit(){
        if(clientUserId != null && clientTaskId != null){
            removeObserver(clientTaskId, clientUserId);
            subOnlineUserNumber();
            System.out.println("******************************************************");
            System.out.println("Client Leave Successfully!");
            System.out.println("******************************************************");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(message);
        if(StringUtils.isNotBlank(message)){
//            PanelTaskMessage taskMessage = (PanelTaskMessage) JSONObject.parse(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    private void removeTaskFromMap(Long taskId){
        taskDetailMap.remove(taskId);
        observerListMap.remove(taskId);
    }

    private void putTaskInfoIntoMap(Long taskId, IWebsocketTaskObserver observer){
        TaskDetailVO detailVO = new TaskDetailVO(taskDetailService.findTaskDetailByTaskId(taskId));
        taskDetailMap.put(taskId, detailVO);
        List<IWebsocketTaskObserver> list = new ArrayList<>();
        list.add(observer);
        observerListMap.put(taskId, list);
    }

    private static synchronized void addOnlineUserNumber(){
        onlineUserNumber++;
    }

    private static synchronized void subOnlineUserNumber(){
        onlineUserNumber--;
    }

    private static synchronized Integer getOnlineUserNumber(){
        return onlineUserNumber;
    }

    /**
    * 不建议使用！
    * */
    public static void removeUserFromMap(Long userId) throws Exception {
        for (Map.Entry<Long, List<IWebsocketTaskObserver>> next : observerListMap.entrySet()) {
            Long taskId = null;
            for (IWebsocketTaskObserver item : next.getValue()) {
                if (item.isUser(userId)) {
                    taskId = next.getKey();
                }
            }
            if (taskId != null) {
                break;
            }
        }
        throw new Exception("this function is not completed!");
    }

    public static void notifyObserver(Long taskId, Long userId, PanelTaskMessage message){
        if(observerListMap.containsKey(taskId)){
            List<IWebsocketTaskObserver> list = observerListMap.get(taskId);
            for(IWebsocketTaskObserver item : list){
                if(!item.isUser(userId)){
                    item.sendMessage(message);
                }
            }
        }
    }

    public static void sendMessageToUser(Long taskId, Long userId, PanelTaskMessage message){
        if(observerListMap.containsKey(taskId)){
            List<IWebsocketTaskObserver> observerList = observerListMap.get(taskId);
            for(IWebsocketTaskObserver item : observerList){
                if(item.isUser(userId)){
                    item.sendMessage(message);
                    break;
                }
            }
        }
    }

    @Override
    public void registerObserver(Long taskId, IWebsocketTaskObserver observer) {
        if(observerListMap.containsKey(taskId)){
            observerListMap.get(taskId).add(observer);
        }else{
            putTaskInfoIntoMap(taskId, observer);
        }
    }

    @Override
    public void removeObserver(Long taskId, Long userId) {
        if(observerListMap.containsKey(taskId)){
            List<IWebsocketTaskObserver> iWebsocketTaskObserverList = observerListMap.get(taskId);
            for (IWebsocketTaskObserver item : iWebsocketTaskObserverList){
                if(item.isUser(userId)){
                    iWebsocketTaskObserverList.remove(item);
                    break;
                }
            }
            if(iWebsocketTaskObserverList.isEmpty()){
                removeTaskFromMap(taskId);
            }else{
                observerListMap.put(taskId, iWebsocketTaskObserverList);
            }
        }
    }
}
