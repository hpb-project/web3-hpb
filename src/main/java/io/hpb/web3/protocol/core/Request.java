package io.hpb.web3.protocol.core;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import io.hpb.web3.protocol.Web3Service;
import io.reactivex.Flowable;


public class Request<S, T extends Response> {
    private static AtomicLong nextId = new AtomicLong(0);

    private String jsonrpc = "2.0";
    private String Method;
    private List<S> params;
    private long id;

    private Web3Service web3Service;

    
    
    private Class<T> responseType;

    public Request() {
    }

    public Request(String Method, List<S> params,
                   Web3Service web3Service, Class<T> type) {
        this.Method = Method;
        this.params = params;
        this.id = nextId.getAndIncrement();
        this.web3Service = web3Service;
        this.responseType = type;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String Method) {
        this.Method = Method;
    }

    public List<S> getParams() {
        return params;
    }

    public void setParams(List<S> params) {
        this.params = params;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public T send() throws IOException {
        return web3Service.send(this, responseType);
    }

    public CompletableFuture<T> sendAsync() {
        return  web3Service.sendAsync(this, responseType);
    }

    public Flowable<T> flowable() {
        return new RemoteCall<>(this::send).flowable();
    }
}