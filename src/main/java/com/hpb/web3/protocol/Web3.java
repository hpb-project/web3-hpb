package com.hpb.web3.protocol;

import java.util.concurrent.ScheduledExecutorService;

import com.hpb.web3.protocol.core.Hpbereum;
import com.hpb.web3.protocol.core.JsonRpc2_0Web3;
import com.hpb.web3.protocol.rx.Web3Rx;


public interface Web3 extends Hpbereum, Web3Rx {

    
    static Web3 build(Web3Service web3Service) {
        return new JsonRpc2_0Web3(web3Service);
    }

    
    static Web3 build(
            Web3Service web3Service, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0Web3(web3Service, pollingInterval, scheduledExecutorService);
    }

    
    void shutdown();
}
