package io.hpb.web3.protocol.core;
import java.util.List;
import java.util.concurrent.Callable;

import io.hpb.web3.abi.FunctionEncoder;
import io.hpb.web3.abi.FunctionReturnDecoder;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
public class RemoteFunctionCall<T> extends RemoteCall<T> {
    private final Function function;
    public RemoteFunctionCall(Function function, Callable<T> callable) {
        super(callable);
        this.function = function;
    }
    public String encodeFunctionCall() {
        return FunctionEncoder.encode(function);
    }
    public List<Type> decodeFunctionResponse(String response) {
        return FunctionReturnDecoder.decode(response, function.getOutputParameters());
    }
}
