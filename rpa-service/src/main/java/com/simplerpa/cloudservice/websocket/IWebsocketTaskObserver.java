package com.simplerpa.cloudservice.websocket;

import com.simplerpa.cloudservice.entity.PanelTaskMessage;

import javax.websocket.Session;

public interface IWebsocketTaskObserver {
    public void sendMessage(PanelTaskMessage message);
    public Boolean isUser(Long id);
    public void changeSession(Session session);
}
