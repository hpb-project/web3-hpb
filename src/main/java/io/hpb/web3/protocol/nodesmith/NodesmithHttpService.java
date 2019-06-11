package io.hpb.web3.protocol.nodesmith;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import io.hpb.web3.protocol.http.HttpService;
import okhttp3.Headers;
import okhttp3.OkHttpClient;


public class NodesmithHttpService extends HttpService {

    static final String NS_RATELIMIT_LIMIT = "x-ratelimit-limit";
    static final String NS_RATELIMIT_REMAINING = "x-ratelimit-remaining";
    static final String NS_RATELIMIT_RESET = "x-ratelimit-reset";

    private RateLimitInfo latestRateLimitInfo = null;

    public NodesmithHttpService(String url, OkHttpClient httpClient) {
        super(url, httpClient);
    }

    public NodesmithHttpService(String url) {
        super(url);
    }

    public RateLimitInfo getLatestRateLimitInfo() {
        return this.latestRateLimitInfo;
    }

    @Override
    protected void processHeaders(Headers headers) {
        Optional<RateLimitInfo> info = createRateLimitFromHeaders(headers);
        if (info.isPresent()) {
            this.latestRateLimitInfo = info.get();
        }
    }

    static Optional<RateLimitInfo> createRateLimitFromHeaders(Headers headers) {
        if (headers == null) {
            return Optional.empty();
        }

        Set<String> names = headers.names();
        if (!names.containsAll(Arrays.asList(
                NS_RATELIMIT_LIMIT, NS_RATELIMIT_REMAINING, NS_RATELIMIT_RESET))) {
            return Optional.empty();
        }

        return RateLimitInfo.createFromHeaders(
                headers.get(NS_RATELIMIT_LIMIT),
                headers.get(NS_RATELIMIT_REMAINING),
                headers.get(NS_RATELIMIT_RESET));

    }
}
