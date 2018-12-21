package io.hpb.web3.protocol.prometheus;

import io.hpb.web3.protocol.Web3Service;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.JsonRpc2_0Web3;
import io.hpb.web3.protocol.core.Request;
import io.hpb.web3.protocol.core.methods.response.HpbGetCandidateNodes;
import io.hpb.web3.protocol.core.methods.response.HpbGetHpbNodes;

import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;

public class JsonRpc2_0Prometheus extends JsonRpc2_0Web3 implements Prometheus {

    public JsonRpc2_0Prometheus(Web3Service web3Service) {
        super(web3Service);
    }

    public JsonRpc2_0Prometheus(Web3Service web3Service, long pollingInterval,
                                ScheduledExecutorService scheduledExecutorService) {
        super(web3Service, pollingInterval, scheduledExecutorService);
    }

    @Override
    public Request<?, HpbGetCandidateNodes> hpbGetCandidateNodes(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("prometheus_getCandidateNodes",      Arrays.asList(defaultBlockParameter), web3Service, HpbGetCandidateNodes.class);
    }

    @Override
    public Request<?, HpbGetHpbNodes> hpbGetHpbNodes(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("prometheus_getHpbNodes",   Arrays.asList(defaultBlockParameter), web3Service, HpbGetHpbNodes.class);
    }
}
