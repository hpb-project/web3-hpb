package com.hpb.web3.protocol.core;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import rx.Observable;

import com.hpb.web3.utils.Async;


public class RemoteCall<T> {

    private Callable<T> callable;

    public RemoteCall(Callable<T> callable) {
        this.callable = callable;
    }

    
    public T send() throws Exception {
        return callable.call();
    }

    
    public CompletableFuture<T> sendAsync() {
        return Async.run(this::send);
    }

    
    public Observable<T> observable() {
        return Observable.create(
                subscriber -> {
                    try {
                        subscriber.onNext(send());
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
        );
    }
}
