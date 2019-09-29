package io.hpb.web3.protocol.ipc;
public class WindowsIpcService extends IpcService {
    private final String ipcSocketPath;
    public WindowsIpcService(String ipcSocketPath) {
        super();
        this.ipcSocketPath = ipcSocketPath;
    }
    public WindowsIpcService(String ipcSocketPath, boolean includeRawResponse) {
    	super(includeRawResponse,"BODY");
        this.ipcSocketPath = ipcSocketPath;
    }
    public WindowsIpcService(String ipcSocketPath, boolean includeRawResponse,String logLevel) {
    	super(includeRawResponse,logLevel);
    	this.ipcSocketPath = ipcSocketPath;
    }
    @Override
    protected IOFacade getIO() {
        return new WindowsNamedPipe(ipcSocketPath);
    }
}
