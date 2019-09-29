package io.hpb.web3.abi.datatypes.primitive;
import io.hpb.web3.abi.datatypes.NumericType;
public final class Double extends Number<java.lang.Double> {
    public Double(double value) {
        super(value);
    }
    @Override
    public NumericType toSolidityType() {
        throw new UnsupportedOperationException("Fixed types are not supported");
    }
}
