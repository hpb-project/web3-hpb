package io.hpb.web3.abi;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.hpb.web3.abi.datatypes.AbiTypes;
import io.hpb.web3.abi.datatypes.DynamicArray;
import io.hpb.web3.abi.datatypes.StaticArray;
public abstract class TypeReference<T extends io.hpb.web3.abi.datatypes.Type>
        implements Comparable<TypeReference<T>> {
    protected static Pattern ARRAY_SUFFIX = Pattern.compile("\\[(\\d*)]");
    private final Type type;
    private final boolean indexed;
    protected TypeReference() {
        this(false);
    }
    protected TypeReference(boolean indexed) {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        this.indexed = indexed;
    }
    TypeReference getSubTypeReference() {
        return null;
    }
    public int compareTo(TypeReference<T> o) {
        return 0;
    }
    public Type getType() {
        return type;
    }
    public boolean isIndexed() {
        return indexed;
    }
    @SuppressWarnings("unchecked")
    public Class<T> getClassType() throws ClassNotFoundException {
        Type clsType = getType();
        if (getType() instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) clsType).getRawType();
        } else {
            return (Class<T>) Class.forName(clsType.getTypeName());
        }
    }
    public static <T extends io.hpb.web3.abi.datatypes.Type> TypeReference<T> create(Class<T> cls) {
        return create(cls, false);
    }
    public static <T extends io.hpb.web3.abi.datatypes.Type> TypeReference<T> create(
            Class<T> cls, boolean indexed) {
        return new TypeReference<T>(indexed) {
            public java.lang.reflect.Type getType() {
                return cls;
            }
        };
    }
    protected static Class<? extends io.hpb.web3.abi.datatypes.Type> getAtomicTypeClass(
            String solidityType, boolean primitives) throws ClassNotFoundException {
        if (ARRAY_SUFFIX.matcher(solidityType).find()) {
            throw new ClassNotFoundException(
                    "getAtomicTypeClass does not work with array types."
                            + " See makeTypeReference()");
        } else {
            return AbiTypes.getType(solidityType, primitives);
        }
    }
    public abstract static class StaticArrayTypeReference<T extends io.hpb.web3.abi.datatypes.Type>
            extends TypeReference<T> {
        private final int size;
        protected StaticArrayTypeReference(int size) {
            this.size = size;
        }
        public int getSize() {
            return size;
        }
    }
    public static TypeReference makeTypeReference(String solidityType)
            throws ClassNotFoundException {
        return makeTypeReference(solidityType, false, false);
    }
    public static TypeReference makeTypeReference(
            String solidityType, final boolean indexed, final boolean primitives)
            throws ClassNotFoundException {
        Matcher nextSquareBrackets = ARRAY_SUFFIX.matcher(solidityType);
        if (!nextSquareBrackets.find()) {
            final Class<? extends io.hpb.web3.abi.datatypes.Type> typeClass =
                    getAtomicTypeClass(solidityType, primitives);
            return create(typeClass, indexed);
        }
        int lastReadStringPosition = nextSquareBrackets.start();
        final Class<? extends io.hpb.web3.abi.datatypes.Type> baseClass =
                getAtomicTypeClass(solidityType.substring(0, lastReadStringPosition), primitives);
        TypeReference arrayWrappedType = create(baseClass, indexed);
        final int len = solidityType.length();
        while (lastReadStringPosition < len) {
            String arraySize = nextSquareBrackets.group(1);
            final TypeReference baseTr = arrayWrappedType;
            if (arraySize == null || arraySize.equals("")) {
                arrayWrappedType =
                        new TypeReference<DynamicArray>(indexed) {
                            @Override
                            TypeReference getSubTypeReference() {
                                return baseTr;
                            }
                            @Override
                            public java.lang.reflect.Type getType() {
                                return new ParameterizedType() {
                                    @Override
                                    public java.lang.reflect.Type[] getActualTypeArguments() {
                                        return new java.lang.reflect.Type[] {baseTr.getType()};
                                    }
                                    @Override
                                    public java.lang.reflect.Type getRawType() {
                                        return DynamicArray.class;
                                    }
                                    @Override
                                    public java.lang.reflect.Type getOwnerType() {
                                        return Class.class;
                                    }
                                };
                            }
                        };
            } else {
                final Class arrayclass;
                int arraySizeInt = Integer.parseInt(arraySize);
                if (arraySizeInt <= StaticArray.MAX_SIZE_OF_STATIC_ARRAY) {
                    arrayclass =
                            Class.forName(
                                    "io.hpb.web3.abi.datatypes.generated.StaticArray" + arraySize);
                } else {
                    arrayclass = StaticArray.class;
                }
                arrayWrappedType =
                        new TypeReference.StaticArrayTypeReference<StaticArray>(arraySizeInt) {
                            @Override
                            TypeReference getSubTypeReference() {
                                return baseTr;
                            }
                            @Override
                            public boolean isIndexed() {
                                return indexed;
                            }
                            @Override
                            public java.lang.reflect.Type getType() {
                                return new ParameterizedType() {
                                    @Override
                                    public java.lang.reflect.Type[] getActualTypeArguments() {
                                        return new java.lang.reflect.Type[] {baseTr.getType()};
                                    }
                                    @Override
                                    public java.lang.reflect.Type getRawType() {
                                        return arrayclass;
                                    }
                                    @Override
                                    public java.lang.reflect.Type getOwnerType() {
                                        return Class.class;
                                    }
                                };
                            }
                        };
            }
            lastReadStringPosition = nextSquareBrackets.end();
            nextSquareBrackets = ARRAY_SUFFIX.matcher(solidityType);
            if (!nextSquareBrackets.find(lastReadStringPosition) && lastReadStringPosition != len) {
                throw new ClassNotFoundException(
                        "Unable to make TypeReference from " + solidityType);
            }
        }
        return arrayWrappedType;
    }
}
