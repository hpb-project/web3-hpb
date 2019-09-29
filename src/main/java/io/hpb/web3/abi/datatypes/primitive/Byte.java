package io.hpb.web3.abi.datatypes.primitive;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.generated.Bytes1;
public final class Byte extends PrimitiveType<java.lang.Byte> {
    public Byte(byte value) {
        super(value);
    }
    @Override
    public Type toSolidityType() {
        return new Bytes1(new byte[] {getValue()});
    }
}
