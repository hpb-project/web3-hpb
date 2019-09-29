package io.hpb.web3.crypto;
import java.math.BigInteger;
public class ECDSASignature {
    public final BigInteger r;
    public final BigInteger s;
    public ECDSASignature(BigInteger r, BigInteger s) {
        this.r = r;
        this.s = s;
    }
    public boolean isCanonical() {
        return s.compareTo(Sign.HALF_CURVE_ORDER) <= 0;
    }
    public ECDSASignature toCanonicalised() {
        if (!isCanonical()) {
            return new ECDSASignature(r, Sign.CURVE.getN().subtract(s));
        } else {
            return this;
        }
    }
}
