package io.hpb.web3.spring.actuate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.util.Assert;

import io.hpb.web3.protocol.Web3;


public class Web3HealthIndicator extends AbstractHealthIndicator {

    private Web3 web3;

    public Web3HealthIndicator(Web3 web3) {
        Assert.notNull(web3, "Web3 must not be null");
        this.web3 = web3;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try {
            boolean listening = web3.netListening().send().isListening();
            if (!listening) {
                builder.down();
            } else {
                builder.up();
                List<CompletableFuture> futures = new ArrayList<>();

                futures.add(web3.netVersion()
                        .sendAsync()
                        .thenApply(netVersion ->
                                builder.withDetail("netVersion", netVersion.getNetVersion())));

                futures.add(web3.web3ClientVersion()
                        .sendAsync()
                        .thenApply(web3ClientVersion ->
                                builder.withDetail("clientVersion", web3ClientVersion.getWeb3ClientVersion())));

                futures.add(web3.hpbBlockNumber()
                        .sendAsync()
                        .thenApply(hpbBlockNumber ->
                                builder.withDetail("blockNumber", hpbBlockNumber.getBlockNumber())));

                futures.add(web3.hpbProtocolVersion()
                        .sendAsync()
                        .thenApply(hpbProtocolVersion ->
                                builder.withDetail("protocolVersion", hpbProtocolVersion.getProtocolVersion())));

                futures.add(web3.netPeerCount()
                        .sendAsync()
                        .thenApply(netPeerCount ->
                                builder.withDetail("netPeerCount", netPeerCount.getQuantity())));

                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).get();
            }

        } catch (Exception ex) {
            builder.down(ex);
        }
    }
}
