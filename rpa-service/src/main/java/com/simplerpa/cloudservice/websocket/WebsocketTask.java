package com.simplerpa.cloudservice.websocket;

import com.simplerpa.cloudservice.entity.PanelTaskMessage;
import com.simplerpa.cloudservice.entity.VO.TaskDetailVO;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月21日 21:35
 */

@ServerEndpoint("/task/websocket")
@Component
public class WebsocketTask implements IWebsocketTaskSubject {
    private static Integer onlineUserNumber = 0;
    private static ConcurrentHashMap<Long, TaskDetailVO> taskDetailMap;
    private static ConcurrentHashMap<Long, List<IWebsocketTaskObserver> > observerListMap;

    private void removeTaskFromMap(Long taskId){
        taskDetailMap.remove(taskId);
        observerListMap.remove(taskId);
    }

    private static synchronized void addOnlineUserNumber(){
        onlineUserNumber++;
    }

    private static synchronized Integer getOnlineUserNumber(){
        return onlineUserNumber;
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

    @Override
    public void registerObserver(IWebsocketTaskObserver observer) {

    }

    @Override
    public void removeObserver(Long taskId, Long userId) {
        if(observerListMap.containsKey(taskId)){
            List<IWebsocketTaskObserver> iWebsocketTaskObserverList = WebsocketTask.observerListMap.get(taskId);
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
