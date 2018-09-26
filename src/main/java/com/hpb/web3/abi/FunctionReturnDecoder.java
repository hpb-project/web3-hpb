package com.hpb.web3.abi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hpb.web3.abi.datatypes.Array;
import com.hpb.web3.abi.datatypes.Bytes;
import com.hpb.web3.abi.datatypes.BytesType;
import com.hpb.web3.abi.datatypes.DynamicArray;
import com.hpb.web3.abi.datatypes.DynamicBytes;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;
import com.hpb.web3.abi.datatypes.Utf8String;
import com.hpb.web3.abi.datatypes.generated.Bytes32;
import com.hpb.web3.utils.Numeric;
import com.hpb.web3.utils.Strings;

import static com.hpb.web3.abi.TypeDecoder.MAX_BYTE_LENGTH_FOR_HEX_STRING;


public class FunctionReturnDecoder {

    private FunctionReturnDecoder() { }

    
    public static List<Type> decode(
            String rawInput, List<TypeReference<Type>> outputParameters) {
        String input = Numeric.cleanHexPrefix(rawInput);

        if (Strings.isEmpty(input)) {
            return Collections.emptyList();
        } else {
            return build(input, outputParameters);
        }
    }

    
    @SuppressWarnings("unchecked")
    public static <T extends Type> Type decodeIndexedValue(
            String rawInput, TypeReference<T> typeReference) {
        String input = Numeric.cleanHexPrefix(rawInput);

        try {
            Class<T> type = typeReference.getClassType();

            if (Bytes.class.isAssignableFrom(type)) {
                return TypeDecoder.decodeBytes(input, (Class<Bytes>) Class.forName(type.getName()));
            } else if (Array.class.isAssignableFrom(type)
                    || BytesType.class.isAssignableFrom(type)
                    || Utf8String.class.isAssignableFrom(type)) {
                return TypeDecoder.decodeBytes(input, Bytes32.class);
            } else {
                return TypeDecoder.decode(input, type);
            }
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Invalid class reference provided", e);
        }
    }

    private static List<Type> build(
            String input, List<TypeReference<Type>> outputParameters) {
        List<Type> results = new ArrayList<>(outputParameters.size());

        int offset = 0;
        for (TypeReference<?> typeReference:outputParameters) {
            try {
                @SuppressWarnings("unchecked")
                Class<Type> type = (Class<Type>) typeReference.getClassType();

                int hexStringDataOffset = getDataOffset(input, offset, type);

                Type result;
                if (DynamicArray.class.isAssignableFrom(type)) {
                    result = TypeDecoder.decodeDynamicArray(
                            input, hexStringDataOffset, typeReference);
                    offset += MAX_BYTE_LENGTH_FOR_HEX_STRING;
                } else if (typeReference instanceof TypeReference.StaticArrayTypeReference) {
                    int length = ((TypeReference.StaticArrayTypeReference) typeReference).getSize();
                    result = TypeDecoder.decodeStaticArray(
                            input, hexStringDataOffset, typeReference, length);
                    offset += length * MAX_BYTE_LENGTH_FOR_HEX_STRING;
                } else if (StaticArray.class.isAssignableFrom(type)) {
                    int length = Integer.parseInt(type.getSimpleName()
                            .substring(StaticArray.class.getSimpleName().length()));
                    result = TypeDecoder.decodeStaticArray(
                            input, hexStringDataOffset, typeReference, length);
                    offset += length * MAX_BYTE_LENGTH_FOR_HEX_STRING;
                } else {
                    result = TypeDecoder.decode(input, hexStringDataOffset, type);
                    offset += MAX_BYTE_LENGTH_FOR_HEX_STRING;
                }
                results.add(result);

            } catch (ClassNotFoundException e) {
                throw new UnsupportedOperationException("Invalid class reference provided", e);
            }
        }
        return results;
    }

    private static <T extends Type> int getDataOffset(String input, int offset, Class<T> type) {
        if (DynamicBytes.class.isAssignableFrom(type)
                || Utf8String.class.isAssignableFrom(type)
                || DynamicArray.class.isAssignableFrom(type)) {
            return TypeDecoder.decodeUintAsInt(input, offset) << 1;
        } else {
            return offset;
        }
    }
}
