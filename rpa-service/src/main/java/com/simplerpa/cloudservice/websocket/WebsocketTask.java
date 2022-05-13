package com.simplerpa.cloudservice.websocket;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.StringUtils;
import com.simplerpa.cloudservice.entity.PanelTaskMessage;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import com.simplerpa.cloudservice.entity.util.DictionaryUtil;
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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月21日 21:35
 */

@ServerEndpoint("/task/websocket/{taskJsonInfo}")
@Component
public class WebsocketTask implements IWebsocketTaskSubject {
    private static ITaskDetailService taskDetailService;

    private static AtomicInteger onlineUserNumber = new AtomicInteger(0);
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
            WebsocketTaskClient client = new WebsocketTaskClient(session, userId);
            registerObserver(taskId, client);
            try{
                client.sendMessage(new PanelTaskMessage(DictionaryUtil.TASK_MESSAGE_OK, "Connection is created successfully!"));
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

    @Override
    public void registerObserver(Long taskId, IWebsocketTaskObserver observer) {
        final String taskIdStr = clientTaskId.toString().intern();
        synchronized (taskIdStr){
            if(observerListMap.containsKey(taskId)){
                List<IWebsocketTaskObserver> arrList = observerListMap.get(taskId);
                boolean isChanged = false;
                for(int i = 0; i<arrList.size(); i++){
                    if(arrList.get(i).isSameUser(observer)){
                        arrList.set(i, observer);
                        isChanged = true;
                        break;
                    }
                }
                if(!isChanged){
                    arrList.add(observer);
                    addOnlineUserNumber();
                }
//                observerListMap.put(taskId, arrList);
            }else{
                putTaskInfoIntoMap(taskId, observer);
                addOnlineUserNumber();
            }
        }
    }

    @Override
    public void removeObserver(Long taskId, Long userId) {
        final String taskIdStr = clientTaskId.toString().intern();
        synchronized (taskIdStr){
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
                }
            }
        }
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

    private static void addOnlineUserNumber(){
        onlineUserNumber.getAndIncrement();
    }

    private static void subOnlineUserNumber(){
        onlineUserNumber.getAndDecrement();
    }

    private static int getOnlineUserNumber(){
        return onlineUserNumber.get();
    }

    public static void notifyObserver(Long taskId, PanelTaskMessage message){
        if(observerListMap.containsKey(taskId)){
            List<IWebsocketTaskObserver> list = observerListMap.get(taskId);
            for(IWebsocketTaskObserver item : list){
                item.sendMessage(message);
            }
        }
    }

    public static void sendMessageToOtherUser(Long taskId, Long userId, PanelTaskMessage message){
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
    /*
    public static IWebsocketTaskObserver findTaskObserver(Long taskId, Long userId){
        if(taskId == null || userId == null){
            return null;
        }
        if(observerListMap.containsKey(taskId)){
            List<IWebsocketTaskObserver> observerList = observerListMap.get(taskId);
            for(IWebsocketTaskObserver item : observerList){
                if(item.isUser(userId)){
                    return item;
                }
            }
        }
        return null;
    }*/
}
