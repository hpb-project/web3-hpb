package io.hpb.web3.protocol.websocket.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification<T> {
    private String jsonrpc;
    private String Method;
    private NotificationParams<T> params;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public String getMethod() {
        return Method;
    }

    public NotificationParams<T> getParams() {
        return params;
    }
}

