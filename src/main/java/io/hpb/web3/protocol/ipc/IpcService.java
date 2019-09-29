package io.hpb.web3.protocol.ipc;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.hpb.web3.protocol.Service;
public class IpcService extends Service {
    private static final Logger log = LoggerFactory.getLogger(IpcService.class);
    private String logLevel;
    public IpcService(boolean includeRawResponses,String logLevel ) {
        super(includeRawResponses);
        if(StringUtils.isBlank(logLevel)) {
        	this.logLevel="BODY";
        }else {
        	this.logLevel=logLevel;
        }
    }
    public IpcService() {
        this(false,"BODY");
    }
    protected IOFacade getIO() {
        throw new UnsupportedOperationException("not implemented");
    }
    @Override
    protected InputStream performIO(String payload) throws IOException {
        IOFacade io = getIO();
        io.write(payload);
        if("BODY".equalsIgnoreCase(logLevel)){
        	log.debug(">> " + payload);
        }
        String result = io.read();
        if("BODY".equalsIgnoreCase(logLevel)){
        	log.debug("<< " + result);
        }
        io.close();
        return new ByteArrayInputStream(result.getBytes("UTF-8"));
    }
    @Override
    public void close() throws IOException {}
}