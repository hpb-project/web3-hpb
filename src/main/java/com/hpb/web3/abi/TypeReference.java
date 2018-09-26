package com.hpb.web3.abi;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class TypeReference<T extends com.hpb.web3.abi.datatypes.Type>
        implements Comparable<TypeReference<T>> {

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

    public int compareTo(TypeReference<T> o) {
        // taken from the blog post comments - this results in an errror if the
        // type parameter is left out.
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

    public static <T extends com.hpb.web3.abi.datatypes.Type> TypeReference<T> create(Class<T> cls) {
        return new TypeReference<T>() {
            @Override
            public Type getType() {
                return cls;
            }
        };
    }

    public abstract static class StaticArrayTypeReference<T extends com.hpb.web3.abi.datatypes.Type>
            extends TypeReference<T> {

        private final int size;

        protected StaticArrayTypeReference(int size) {
            super();
            this.size = size;
        }

        public int getSize() {
            return size;
        }
    }
}
