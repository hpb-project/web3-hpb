package io.hpb.web3.abi.datatypes.primitive;
import io.hpb.web3.abi.datatypes.NumericType;
import io.hpb.web3.abi.datatypes.generated.Int64;
public final class Long extends Number<java.lang.Long> {
    public Long(long value) {
        super(value);
    }
    @Override
    public NumericType toSolidityType() {
        return new Int64(getValue());
    }
}
