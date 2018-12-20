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
            // The order of the curve is the number of valid points that exist on that curve.
            // If S is in the upper half of the number of valid points, then bring it back to
            // the lower half. Otherwise, imagine that
            //    N = 10
            //    s = 8, so (-8 % 10 == 2) thus both (r, 8) and (r, 2) are valid solutions.
            //    10 - 8 == 2, giving us always the latter solution, which is canonical.
            return new ECDSASignature(r, Sign.CURVE.getN().subtract(s));
        } else {
            return this;
        }
    }
}
