package io.hpb.web3.protocol.ipc;


public class UnixIpcService extends IpcService {
    private final String ipcSocketPath;

    public UnixIpcService(String ipcSocketPath) {
        super();
        this.ipcSocketPath = ipcSocketPath;
    }

    public UnixIpcService(String ipcSocketPath, boolean includeRawResponse) {
        super(includeRawResponse);
        this.ipcSocketPath = ipcSocketPath;
    }

    @Override
    protected IOFacade getIO() {
        return new UnixDomainSocket(ipcSocketPath);
    }
}
