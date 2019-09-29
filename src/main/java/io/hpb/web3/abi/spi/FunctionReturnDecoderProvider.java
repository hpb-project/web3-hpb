package io.hpb.web3.abi.spi;
import java.util.function.Supplier;

import io.hpb.web3.abi.FunctionReturnDecoder;
public interface FunctionReturnDecoderProvider extends Supplier<FunctionReturnDecoder> {}
