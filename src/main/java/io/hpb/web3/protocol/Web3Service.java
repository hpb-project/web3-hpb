package io.hpb.web3.protocol;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import io.hpb.web3.protocol.core.Request;
import io.hpb.web3.protocol.core.Response;
import io.hpb.web3.protocol.websocket.events.Notification;
import io.reactivex.Flowable;


public interface Web3Service {

    
    <T extends Response> T send(
            Request request, Class<T> responseType) throws IOException;

    
    <T extends Response> CompletableFuture<T> sendAsync(
            Request request, Class<T> responseType);

    
    <T extends Notification<?>> Flowable<T> subscribe(
            Request request,
            String unsubscribeMethod,
            Class<T> responseType);

    
    void close() throws IOException;
}
