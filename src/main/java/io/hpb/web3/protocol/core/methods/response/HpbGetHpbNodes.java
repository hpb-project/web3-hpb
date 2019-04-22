package io.hpb.web3.protocol.core.methods.response;

import java.util.List;

import io.hpb.web3.protocol.core.Response;
/**
 * @author will
 *   get hpb nodes
 */
public class HpbGetHpbNodes extends Response< List<String>> {
    public List<String> getHpbNodes() {
        return getResult();
    }
}
