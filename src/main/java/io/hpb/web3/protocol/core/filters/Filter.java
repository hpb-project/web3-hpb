package io.hpb.web3.protocol.core.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.hpb.web3.protocol.Web3;

import io.hpb.web3.protocol.core.Request;
import io.hpb.web3.protocol.core.Response;
import io.hpb.web3.protocol.core.Response.Error;
import io.hpb.web3.protocol.core.RpcErrors;
import io.hpb.web3.protocol.core.methods.response.HpbFilter;
import io.hpb.web3.protocol.core.methods.response.HpbLog;
import io.hpb.web3.protocol.core.methods.response.HpbUninstallFilter;



public abstract class Filter<T> {

    private static final Logger log = LoggerFactory.getLogger(Filter.class);

    final Web3 web3;
    final Callback<T> callback;

    private volatile BigInteger filterId;

    private ScheduledFuture<?> schedule;
    
    private ScheduledExecutorService scheduledExecutorService;

    private long blockTime;

    public Filter(Web3 web3, Callback<T> callback) {
        this.web3 = web3;
        this.callback = callback;
    }

    public void run(ScheduledExecutorService scheduledExecutorService, long blockTime) {
        try {
            HpbFilter hpbFilter = sendRequest();
            if (hpbFilter.hasError()) {
                throwException(hpbFilter.getError());
            }

            filterId = hpbFilter.getFilterId();
            this.scheduledExecutorService = scheduledExecutorService;
            this.blockTime = blockTime;
            // this runs in the caller thread as if any exceptions are encountered, we shouldn't
            // proceed with creating the scheduled task below
            getInitialFilterLogs();

            
            schedule = scheduledExecutorService.scheduleAtFixedRate(
                    () -> {
                        try {
                            this.pollFilter(hpbFilter);
                        } catch (Throwable e) {
                            // All exceptions must be caught, otherwise our job terminates without
                            // any notification
                            log.error("Error sending request", e);
                        }
                    },
                    0, blockTime, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            throwException(e);
        }
    }

    private void getInitialFilterLogs() {
        try {
            Optional<Request<?, HpbLog>> maybeRequest = this.getFilterLogs(this.filterId);
            HpbLog hpbLog = null;
            if (maybeRequest.isPresent()) {
                hpbLog = maybeRequest.get().send();
            } else {
                hpbLog = new HpbLog();
                hpbLog.setResult(Collections.emptyList());
            }
            process(hpbLog.getLogs());

        } catch (IOException e) {
            throwException(e);
        }
    }

    private void pollFilter(HpbFilter hpbFilter) {
        HpbLog hpbLog = null;
        try {
            hpbLog = web3.hpbGetFilterChanges(filterId).send();
        } catch (IOException e) {
            throwException(e);
        }
        if (hpbLog.hasError()) {
            Error error = hpbLog.getError();
            switch (error.getCode()) {
                case RpcErrors.FILTER_NOT_FOUND: reinstallFilter();
                    break;
                default: throwException(error);
                    break;
            }
        } else {
            process(hpbLog.getLogs());
        }
    }

    abstract HpbFilter sendRequest() throws IOException;

    abstract void process(List<HpbLog.LogResult> logResults);
    
    private void reinstallFilter() {
        log.warn("The filter has not been found. Filter id: " + filterId);
        schedule.cancel(true);
        this.run(scheduledExecutorService, blockTime);
    }

    public void cancel() {
        schedule.cancel(false);

        try {
            HpbUninstallFilter hpbUninstallFilter = web3.hpbUninstallFilter(filterId).send();
            if (hpbUninstallFilter.hasError()) {
                throwException(hpbUninstallFilter.getError());
            }

            if (!hpbUninstallFilter.isUninstalled()) {
                throw new FilterException("Filter with id '" + filterId + "' failed to uninstall");
            }
        } catch (IOException e) {
            throwException(e);
        }
    }

    
    protected abstract Optional<Request<?, HpbLog>> getFilterLogs(BigInteger filterId);

    void throwException(Response.Error error) {
        throw new FilterException("Invalid request: "
                + (error == null ? "Unknown Error" : error.getMessage()));
    }

    void throwException(Throwable cause) {
        throw new FilterException("Error sending request", cause);
    }
}
