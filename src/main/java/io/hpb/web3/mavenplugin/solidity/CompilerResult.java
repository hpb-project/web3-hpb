package io.hpb.web3.mavenplugin.solidity;


public class CompilerResult {
    public String errors;
    public String output;
    private boolean success = false;

    public CompilerResult(String errors, String output, boolean success) {
        this.errors = errors;
        
        this.output = output.replaceAll("<stdin>:", "");
        this.success = success;
    }

    public boolean isFailed() {
        return !success;
    }
}
