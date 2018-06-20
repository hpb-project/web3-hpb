package com.hpb.web3.protocol.parity;

import java.math.BigInteger;
import java.util.List;

import com.hpb.web3.protocol.core.DefaultBlockParameter;
import com.hpb.web3.protocol.core.Request;
import com.hpb.web3.protocol.core.methods.request.Transaction;
import com.hpb.web3.protocol.parity.methods.request.TraceFilter;
import com.hpb.web3.protocol.parity.methods.response.ParityFullTraceResponse;
import com.hpb.web3.protocol.parity.methods.response.ParityTraceGet;
import com.hpb.web3.protocol.parity.methods.response.ParityTracesResponse;


public interface Trace {
    Request<?, ParityFullTraceResponse> traceCall(
            Transaction transaction,
            List<String> traceTypes,
            DefaultBlockParameter blockParameter);

    Request<?, ParityFullTraceResponse> traceRawTransaction(String data, List<String> traceTypes);

    Request<?, ParityFullTraceResponse> traceReplayTransaction(
            String hash, List<String> traceTypes);

    Request<?, ParityTracesResponse> traceBlock(DefaultBlockParameter blockParameter);

    Request<?, ParityTracesResponse> traceFilter(TraceFilter traceFilter);

    Request<?, ParityTraceGet> traceGet(String hash, List<BigInteger> indices);

    Request<?, ParityTracesResponse> traceTransaction(String hash);
}
