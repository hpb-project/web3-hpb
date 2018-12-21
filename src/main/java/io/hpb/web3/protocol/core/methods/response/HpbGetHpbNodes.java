package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;

import java.util.List;
/**
 * @author will
 *   get hpb nodes
 */
public class HpbGetHpbNodes extends Response< List<String>> {
    public List<String> getHpbNodes() {
        return getResult();
    }
}
