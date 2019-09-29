package io.hpb.web3.abi;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.spi.FunctionReturnDecoderProvider;
public abstract class FunctionReturnDecoder {
    private static FunctionReturnDecoder DEFAULT_DECODER;
    private static final ServiceLoader<FunctionReturnDecoderProvider> loader =
            ServiceLoader.load(FunctionReturnDecoderProvider.class);
    public static List<Type> decode(String rawInput, List<TypeReference<Type>> outputParameters) {
        return decoder().decodeFunctionResult(rawInput, outputParameters);
    }
    public static <T extends Type> Type decodeIndexedValue(
            String rawInput, TypeReference<T> typeReference) {
        return decoder().decodeEventParameter(rawInput, typeReference);
    }
    protected abstract List<Type> decodeFunctionResult(
            String rawInput, List<TypeReference<Type>> outputParameters);
    protected abstract <T extends Type> Type decodeEventParameter(
            String rawInput, TypeReference<T> typeReference);
    private static FunctionReturnDecoder decoder() {
        final Iterator<FunctionReturnDecoderProvider> iterator = loader.iterator();
        return iterator.hasNext() ? iterator.next().get() : defaultDecoder();
    }
    private static FunctionReturnDecoder defaultDecoder() {
        if (DEFAULT_DECODER == null) {
            DEFAULT_DECODER = new DefaultFunctionReturnDecoder();
        }
        return DEFAULT_DECODER;
    }
}
