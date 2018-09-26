package com.hpb.web3.codegen;

import org.slf4j.Logger;


class LogGenerationReporter implements GenerationReporter {

    private final Logger logger;

    public LogGenerationReporter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void report(String msg) {
        logger.warn(msg);
    }
}
