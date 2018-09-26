package com.hpb.web3.protocol.websocket;

import java.util.concurrent.CompletableFuture;


class WebSocketRequest<T> {
    private CompletableFuture<T> onReply;
    private Class<T> responseType;

    public WebSocketRequest(CompletableFuture<T> onReply, Class<T> responseType) {
        this.onReply = onReply;
        this.responseType = responseType;
    }

    public CompletableFuture<T> getOnReply() {
        return onReply;
    }

    public Class<T> getResponseType() {
        return responseType;
    }
}
