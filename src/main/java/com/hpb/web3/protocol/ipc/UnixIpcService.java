package com.hpb.web3.protocol.ipc;


public class UnixIpcService extends IpcService {

    public UnixIpcService(String ipcSocketPath) {
        super(new UnixDomainSocket(ipcSocketPath));
    }

    public UnixIpcService(String ipcSocketPath, boolean includeRawResponse) {
        super(new UnixDomainSocket(ipcSocketPath), includeRawResponse);
    }
}
