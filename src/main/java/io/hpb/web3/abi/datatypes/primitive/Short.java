package io.hpb.web3.abi.datatypes.primitive;
import io.hpb.web3.abi.datatypes.NumericType;
import io.hpb.web3.abi.datatypes.generated.Int16;
public final class Short extends Number<java.lang.Short> {
    public Short(short value) {
        super(value);
    }
    @Override
    public NumericType toSolidityType() {
        return new Int16(getValue());
    }
}
