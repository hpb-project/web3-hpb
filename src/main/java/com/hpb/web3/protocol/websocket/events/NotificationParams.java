package com.hpb.web3.protocol.websocket.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationParams<T> {
    private T result;
    private String subsciption;

    public T getResult() {
        return result;
    }

    public String getSubsciption() {
        return subsciption;
    }
}
