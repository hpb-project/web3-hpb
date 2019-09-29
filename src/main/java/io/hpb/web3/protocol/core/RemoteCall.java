package io.hpb.web3.protocol.core;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import io.hpb.web3.utils.Async;
import io.reactivex.Flowable;
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
    public Flowable<T> flowable() {
        return Flowable.fromCallable(this::send);
    }
}
