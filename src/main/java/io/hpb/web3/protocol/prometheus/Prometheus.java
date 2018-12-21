package io.hpb.web3.protocol.prometheus;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.Web3Service;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.Request;
import io.hpb.web3.protocol.core.methods.response.HpbGetCandidateNodes;
import io.hpb.web3.protocol.core.methods.response.HpbGetHpbNodes;

import java.util.concurrent.ScheduledExecutorService;


public interface Prometheus extends Web3 {

    static Prometheus build(Web3Service web3Service) {
        return new JsonRpc2_0Prometheus(web3Service);
    }

    static Prometheus build(
            Web3Service web3Service, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0Prometheus(web3Service, pollingInterval, scheduledExecutorService);
    }

    public Request<?, HpbGetCandidateNodes> hpbGetCandidateNodes(DefaultBlockParameter defaultBlockParameter);

    public Request<?, HpbGetHpbNodes> hpbGetHpbNodes(DefaultBlockParameter defaultBlockParameter);

}   
