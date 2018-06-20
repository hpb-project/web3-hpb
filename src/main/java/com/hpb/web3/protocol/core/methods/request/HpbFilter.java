package com.hpb.web3.protocol.core.methods.request;

import java.util.Arrays;
import java.util.List;

import com.hpb.web3.protocol.core.DefaultBlockParameter;


public class HpbFilter extends Filter<HpbFilter> {
    private DefaultBlockParameter fromBlock;      private DefaultBlockParameter toBlock;
    private List<String> address;  
    public HpbFilter() {
        super();
    }

    public HpbFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
                     List<String> address) {
        super();
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
        this.address = address;
    }

    public HpbFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock,
                     String address) {
        this(fromBlock, toBlock, Arrays.asList(address));
    }

    public DefaultBlockParameter getFromBlock() {
        return fromBlock;
    }

    public DefaultBlockParameter getToBlock() {
        return toBlock;
    }

    public List<String> getAddress() {
        return address;
    }

    @Override
    HpbFilter getThis() {
        return this;
    }
}
