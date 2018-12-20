package io.hpb.web3.protocol.ipc;

import java.io.IOException;


public interface IOFacade {
    void write(String payload) throws IOException;

    String read() throws IOException;
    
    void close() throws IOException;
}
