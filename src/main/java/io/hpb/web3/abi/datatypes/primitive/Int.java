package io.hpb.web3.abi.datatypes.primitive;
import io.hpb.web3.abi.datatypes.NumericType;
import io.hpb.web3.abi.datatypes.generated.Int32;
public final class Int extends Number<java.lang.Integer> {
    public Int(int value) {
        super(value);
    }
    @Override
    public NumericType toSolidityType() {
        return new Int32(getValue());
    }
}
