package com.simplerpa.cloudservice.websocket;

import com.simplerpa.cloudservice.entity.PanelTaskMessage;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

/**
 * @Description: TODO
 * @author: ChenRui98
 * @date: 2022年04月21日 20:29
 */

public class WebsocketTaskClient implements IWebsocketTaskObserver {
    private Session session = null;
    private Long userId;

    public WebsocketTaskClient(Session session, Long userId){
        setSession(session);
        setUserId(userId);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public void sendMessage(PanelTaskMessage message) {
        try{
            if(session != null){
                session.getBasicRemote().sendObject(message);
            }
        } catch (IOException | EncodeException e){
            e.printStackTrace();
        }
    }

    @Override
    public Boolean isUser(Long id) {
        return id.equals(userId);
    }

    @Override
    public void changeSession(Session session) {
        setSession(session);
    }
}
