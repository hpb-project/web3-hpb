package io.hpb.web3.utils;
import java.math.BigInteger;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
public class Flowables {
    public static Flowable<BigInteger> range(
            final BigInteger startValue, final BigInteger endValue) {
        return range(startValue, endValue, true);
    }
    public static Flowable<BigInteger> range(
            final BigInteger startValue, final BigInteger endValue, final boolean ascending) {
        if (startValue.compareTo(BigInteger.ZERO) == -1) {
            throw new IllegalArgumentException("Negative start index cannot be used");
        } else if (startValue.compareTo(endValue) > 0) {
            throw new IllegalArgumentException(
                    "Negative start index cannot be greater then end index");
        }
        if (ascending) {
            return Flowable.create(
                    subscriber -> {
                        for (BigInteger i = startValue;
                                i.compareTo(endValue) < 1 && !subscriber.isCancelled();
                                i = i.add(BigInteger.ONE)) {
                            subscriber.onNext(i);
                        }
                        if (!subscriber.isCancelled()) {
                            subscriber.onComplete();
                        }
                    },
                    BackpressureStrategy.BUFFER);
        } else {
            return Flowable.create(
                    subscriber -> {
                        for (BigInteger i = endValue;
                                i.compareTo(startValue) > -1 && !subscriber.isCancelled();
                                i = i.subtract(BigInteger.ONE)) {
                            subscriber.onNext(i);
                        }
                        if (!subscriber.isCancelled()) {
                            subscriber.onComplete();
                        }
                    },
                    BackpressureStrategy.BUFFER);
        }
    }
}
