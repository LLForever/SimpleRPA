package com.simplerpa.cloudservice.websocket;

public interface IWebsocketTaskSubject {
    public void registerObserver(Long taskId, IWebsocketTaskObserver observer);
    public void removeObserver(Long taskId, Long userId);
}
