package io.hpb.web3.abi;

import java.util.List;
import java.util.stream.Collectors;

import io.hpb.web3.abi.datatypes.Event;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.crypto.Hash;
import io.hpb.web3.utils.Numeric;


public class EventEncoder {

    private EventEncoder() { }

    public static String encode(Event event) {

        String methodsignature = buildmethodsignature(
                event.getName(),
                event.getParameters());

        return buildEventSignature(methodsignature);
    }

    static <T extends Type> String buildmethodsignature(
            String MethodName, List<TypeReference<T>> parameters) {

        StringBuilder result = new StringBuilder();
        result.append(MethodName);
        result.append("(");
        String params = parameters.stream()
                .map(p -> Utils.getTypeName(p))
                .collect(Collectors.joining(","));
        result.append(params);
        result.append(")");
        return result.toString();
    }

    public static String buildEventSignature(String methodsignature) {
        byte[] input = methodsignature.getBytes();
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash);
    }
}
