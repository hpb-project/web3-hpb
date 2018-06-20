package com.hpb.web3.protocol;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.hpb.web3.protocol.core.Request;
import com.hpb.web3.protocol.core.Response;


public interface Web3Service {
    <T extends Response> T send(
            Request request, Class<T> responseType) throws IOException;

    <T extends Response> CompletableFuture<T> sendAsync(
            Request request, Class<T> responseType);
}
