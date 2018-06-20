package com.hpb.web3.protocol.ipc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpb.web3.protocol.Service;


public class IpcService extends Service {

    private static final Logger log = LoggerFactory.getLogger(IpcService.class);

    private final IOFacade ioFacade;

    public IpcService(IOFacade ioFacade, boolean includeRawResponses) {
        super(includeRawResponses);
        this.ioFacade = ioFacade;
    }

    public IpcService(IOFacade ioFacade) {
        this(ioFacade, false);
    }

    @Override
    protected InputStream performIO(String payload) throws IOException {
        ioFacade.write(payload);
        log.debug(">> " + payload);

        String result = ioFacade.read();
        log.debug("<< " + result);

                        return new ByteArrayInputStream(result.getBytes());
    }
}
