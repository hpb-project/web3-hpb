package io.hpb.web3.protocol.core.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.Request;
import io.hpb.web3.protocol.core.methods.response.HpbFilter;
import io.hpb.web3.protocol.core.methods.response.HpbLog;
import io.hpb.web3.protocol.core.methods.response.Log;


public class LogFilter extends Filter<Log> {

    private final io.hpb.web3.protocol.core.methods.request.HpbFilter hpbFilter;

    public LogFilter(
            Web3 web3, Callback<Log> callback,
            io.hpb.web3.protocol.core.methods.request.HpbFilter hpbFilter) {
        super(web3, callback);
        this.hpbFilter = hpbFilter;
    }


    @Override
    HpbFilter sendRequest() throws IOException {
        return web3.hpbNewFilter(hpbFilter).send();
    }

    @Override
    void process(List<HpbLog.LogResult> logResults) {
        for (HpbLog.LogResult logResult : logResults) {
            if (logResult instanceof HpbLog.LogObject) {
                Log log = ((HpbLog.LogObject) logResult).get();
                callback.onEvent(log);
            } else {
                throw new FilterException(
                        "Unexpected result type: " + logResult.get() + " required LogObject");
            }
        }
    }

    @Override
    protected Optional<Request<?, HpbLog>> getFilterLogs(BigInteger filterId) {
        return Optional.of(web3.hpbGetFilterLogs(filterId));
    }
}
