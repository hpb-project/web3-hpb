package io.hpb.web3.abi;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.Uint;
import io.hpb.web3.crypto.Hash;
import io.hpb.web3.utils.Numeric;


public class FunctionEncoder {

    private FunctionEncoder() { }

    public static String encode(Function function) {
        List<Type> parameters = function.getInputParameters();

        String methodsignature = buildmethodsignature(function.getName(), parameters);
        String MethodId = buildMethodId(methodsignature);

        StringBuilder result = new StringBuilder();
        result.append(MethodId);

        return encodeParameters(parameters, result);
    }

    public static String encodeConstructor(List<Type> parameters) {
        return encodeParameters(parameters, new StringBuilder());
    }

    private static String encodeParameters(List<Type> parameters, StringBuilder result) {
        int dynamicDataOffset = getLength(parameters) * Type.MAX_BYTE_LENGTH;
        StringBuilder dynamicData = new StringBuilder();

        for (Type parameter:parameters) {
            String encodedValue = TypeEncoder.encode(parameter);

            if (TypeEncoder.isDynamic(parameter)) {
                String encodedDataOffset = TypeEncoder.encodeNumeric(
                        new Uint(BigInteger.valueOf(dynamicDataOffset)));
                result.append(encodedDataOffset);
                dynamicData.append(encodedValue);
                dynamicDataOffset += encodedValue.length() >> 1;
            } else {
                result.append(encodedValue);
            }
        }
        result.append(dynamicData);

        return result.toString();
    }

    private static int getLength(List<Type> parameters) {
        int count = 0;
        for (Type type:parameters) {
            if (type instanceof StaticArray) {
                count += ((StaticArray) type).getValue().size();
            } else {
                count++;
            }
        }
        return count;
    }

    static String buildmethodsignature(String MethodName, List<Type> parameters) {
        StringBuilder result = new StringBuilder();
        result.append(MethodName);
        result.append("(");
        String params = parameters.stream()
                .map(Type::getTypeAsString)
                .collect(Collectors.joining(","));
        result.append(params);
        result.append(")");
        return result.toString();
    }

    static String buildMethodId(String methodsignature) {
        byte[] input = methodsignature.getBytes();
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash).substring(0, 10);
    }
}
