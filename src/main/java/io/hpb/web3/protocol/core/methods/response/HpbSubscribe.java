
package io.hpb.web3.protocol.core.methods.response;
import io.hpb.web3.protocol.core.Response;
public class HpbSubscribe extends Response<String> {
    public String getSubscriptionId() {
        return getResult();
    }
}
