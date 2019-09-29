package io.hpb.web3.abi.spi;

import java.util.function.Supplier;

import io.hpb.web3.abi.FunctionEncoder;

public interface FunctionEncoderProvider extends Supplier<FunctionEncoder> {
}
