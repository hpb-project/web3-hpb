package com.hpb.web3.protocol.ipc;


public class WindowsIpcService extends IpcService {

    public WindowsIpcService(String ipcSocketPath) {
        super(new WindowsNamedPipe(ipcSocketPath));
    }

    public WindowsIpcService(String ipcSocketPath, boolean includeRawResponse) {
        super(new WindowsNamedPipe(ipcSocketPath), includeRawResponse);
    }
}
