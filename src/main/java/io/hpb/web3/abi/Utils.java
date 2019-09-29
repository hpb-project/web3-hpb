package io.hpb.web3.abi;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.hpb.web3.abi.datatypes.AbiTypes;
import io.hpb.web3.abi.datatypes.AllReferenceType;
import io.hpb.web3.abi.datatypes.DynamicArray;
import io.hpb.web3.abi.datatypes.DynamicBytes;
import io.hpb.web3.abi.datatypes.Event;
import io.hpb.web3.abi.datatypes.Fixed;
import io.hpb.web3.abi.datatypes.Int;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.Ufixed;
import io.hpb.web3.abi.datatypes.Uint;
import io.hpb.web3.abi.datatypes.Utf8String;
import io.hpb.web3.protocol.core.methods.response.AbiDefinition;
import io.hpb.web3.protocol.core.methods.response.AbiDefinition.NamedType;
import io.hpb.web3.protocol.core.methods.response.Log;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.utils.Numeric;
import io.hpb.web3.utils.ObjectJsonHelper;
public class Utils {
    private Utils() {}
    static <T extends Type> String getTypeName(TypeReference<T> typeReference) {
        try {
            java.lang.reflect.Type reflectedType = typeReference.getType();
            Class<?> type;
            if (reflectedType instanceof ParameterizedType) {
                type = (Class<?>) ((ParameterizedType) reflectedType).getRawType();
                return getParameterizedTypeName(typeReference, type);
            } else {
                type = Class.forName(reflectedType.getTypeName());
                return getSimpleTypeName(type);
            }
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Invalid class reference provided", e);
        }
    }
    static String getSimpleTypeName(Class<?> type) {
        String simpleName = type.getSimpleName().toLowerCase();
        if (type.equals(Uint.class)
                || type.equals(Int.class)
                || type.equals(Ufixed.class)
                || type.equals(Fixed.class)) {
            return simpleName + "256";
        } else if (type.equals(Utf8String.class)) {
            return "string";
        } else if (type.equals(DynamicBytes.class)) {
            return "bytes";
        } else {
            return simpleName;
        }
    }
    static <T extends Type, U extends Type> String getParameterizedTypeName(
            TypeReference<T> typeReference, Class<?> type) {
        try {
            if (type.equals(DynamicArray.class)) {
                Class<U> parameterizedType = getParameterizedTypeFromArray(typeReference);
                String parameterizedTypeName = getSimpleTypeName(parameterizedType);
                return parameterizedTypeName + "[]";
            } else if (type.equals(StaticArray.class)) {
                Class<U> parameterizedType = getParameterizedTypeFromArray(typeReference);
                String parameterizedTypeName = getSimpleTypeName(parameterizedType);
                return parameterizedTypeName
                        + "["
                        + ((TypeReference.StaticArrayTypeReference) typeReference).getSize()
                        + "]";
            } else {
                throw new UnsupportedOperationException("Invalid type provided " + type.getName());
            }
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Invalid class reference provided", e);
        }
    }
    @SuppressWarnings("unchecked")
    static <T extends Type> Class<T> getParameterizedTypeFromArray(TypeReference typeReference)
            throws ClassNotFoundException {
        java.lang.reflect.Type type = typeReference.getType();
        java.lang.reflect.Type[] typeArguments =
                ((ParameterizedType) type).getActualTypeArguments();
        String parameterizedTypeName = typeArguments[0].getTypeName();
        return (Class<T>) Class.forName(parameterizedTypeName);
    }
    @SuppressWarnings("unchecked")
    public static List<TypeReference<Type>> convert(List<TypeReference<?>> input) {
        List<TypeReference<Type>> result = new ArrayList<>(input.size());
        result.addAll(
                input.stream()
                        .map(typeReference -> (TypeReference<Type>) typeReference)
                        .collect(Collectors.toList()));
        return result;
    }
    public static <T, R extends Type<T>, E extends Type<T>> List<E> typeMap(
            List<List<T>> input, Class<E> outerDestType, Class<R> innerType) {
        List<E> result = new ArrayList<>();
        try {
            Constructor<E> constructor =
                    outerDestType.getDeclaredConstructor(Class.class, List.class);
            for (List<T> ts : input) {
                E e = constructor.newInstance(innerType, typeMap(ts, innerType));
                result.add(e);
            }
        } catch (NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            throw new TypeMappingException(e);
        }
        return result;
    }
    public static <T, R extends Type<T>> List<R> typeMap(List<T> input, Class<R> destType)
            throws TypeMappingException {
        List<R> result = new ArrayList<>(input.size());
        if (!input.isEmpty()) {
            try {
                Constructor<R> constructor =
                        destType.getDeclaredConstructor(input.get(0).getClass());
                for (T value : input) {
                    result.add(constructor.newInstance(value));
                }
            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | InvocationTargetException
                    | InstantiationException e) {
                throw new TypeMappingException(e);
            }
        }
        return result;
    }
    public static List<Type> decodeInputData(String data, String abi)
			throws JsonParseException, JsonMappingException, IOException {
		List<TypeReference<?>> inputParameterTypes = buildTypeReferenceTypesFromAbi(abi);
		List<Type> decode = FunctionReturnDecoder.decode(Numeric.cleanHexPrefix(data).substring(8), convert(inputParameterTypes));
		return decode;
	}
    public static List<EventValuesWithLog> decodeEvent(TransactionReceipt transactionReceipt, String abi)
			throws JsonParseException, JsonMappingException, IOException {
		AbiDefinition abiDefinition = ObjectJsonHelper.deserialize(abi, AbiDefinition.class);
		List<NamedType> inputs = abiDefinition.getInputs();
		String eventName = abiDefinition.getName();
		List<TypeReference<?>> inputParameterTypes = buildTypeReferenceTypes(inputs);
		Event event = new Event(eventName,inputParameterTypes);
		List<EventValuesWithLog> list = transactionReceipt.getLogs().stream().map(log -> extractEventParametersWithLog(event, log))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
		return list;
	}
    public static List<TypeReference<?>> buildTypeReferenceTypesFromAbi(String abi)
			throws JsonParseException, JsonMappingException, IOException {
		AbiDefinition abiDefinition = ObjectJsonHelper.deserialize(abi, AbiDefinition.class);
		List<NamedType> inputs = abiDefinition.getInputs();
		List<TypeReference<?>> inputParameterTypes = buildTypeReferenceTypes(inputs);
		return inputParameterTypes;
	}
    public static List<TypeReference<?>> buildTypeReferenceTypes(List<AbiDefinition.NamedType> namedTypes) {
		List<TypeReference<?>> result = new ArrayList<>(namedTypes.size());
        for (int i = 0; i < namedTypes.size(); i++) {
            String type = namedTypes.get(i).getType();
            result.add(buildTypeName(type,namedTypes.get(i).isIndexed()));
        }
        return result;
    }
    @SuppressWarnings({"unchecked" })
    public static TypeReference<Type> buildTypeName(String typeDeclaration, boolean isIndexed) {
		String trimStorageDeclaration = trimStorageDeclaration(typeDeclaration);
		Matcher matcher = pattern.matcher(trimStorageDeclaration);
		if (matcher.find()) {
			String type = matcher.group(1);
			String firstArrayDimension = matcher.group(2);
			String secondArrayDimension = matcher.group(3);
			TypeReference typeReference=null;

			if ("".equals(firstArrayDimension)) {
				typeReference=AllReferenceType.getDynamicType(type,isIndexed);
			} else {
				typeReference=AllReferenceType.getStaticType(type, Integer.valueOf(firstArrayDimension),isIndexed);
			}

			if (secondArrayDimension != null) {//二维数组不支持
				
			}
			return (TypeReference<Type>)typeReference;
		} else {
			return new TypeReference<Type>(isIndexed) {
				@Override
				public java.lang.reflect.Type getType() {
					return AbiTypes.getType(trimStorageDeclaration);
				}};
		}
	}
    private static String trimStorageDeclaration(String type) {
		if (type.endsWith(" storage") || type.endsWith(" memory")) {
			return type.split(" ")[0];
		} else {
			return type;
		}
	}

	private static final String regex = "(\\w+)(?:\\[(.*?)\\])(?:\\[(.*?)\\])?";
	private static final Pattern pattern = Pattern.compile(regex);
	public static EventValuesWithLog extractEventParametersWithLog(Event event, Log log) {
        final EventValues eventValues = staticExtractEventParameters(event, log);
        return (eventValues == null) ? null : new EventValuesWithLog(eventValues, log);
    }
	
	public static class EventValuesWithLog {
		private final EventValues eventValues;
		private final Log log;

		private EventValuesWithLog(EventValues eventValues, Log log) {
			this.eventValues = eventValues;
			this.log = log;
		}

		public List<Type> getIndexedValues() {
			return eventValues.getIndexedValues();
		}

		public List<Type> getNonIndexedValues() {
			return eventValues.getNonIndexedValues();
		}

		public Log getLog() {
			return log;
		}
	}

	public static EventValues staticExtractEventParameters(Event event, Log log) {
		final List<String> topics = log.getTopics();
		String encodedEventSignature = EventEncoder.encode(event);
		if (topics == null || topics.size() == 0 || !topics.get(0).equals(encodedEventSignature)) {
			return null;
		}

		List<Type> indexedValues = new ArrayList<>();
		List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());

		List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
		for (int i = 0; i < indexedParameters.size(); i++) {
			Type value = FunctionReturnDecoder.decodeIndexedValue(topics.get(i + 1), indexedParameters.get(i));
			indexedValues.add(value);
		}
		return new EventValues(indexedValues, nonIndexedValues);
	}
}
