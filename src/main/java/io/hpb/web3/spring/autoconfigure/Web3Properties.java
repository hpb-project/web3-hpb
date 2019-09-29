package io.hpb.web3.spring.autoconfigure;

import static io.hpb.web3.spring.autoconfigure.Web3Properties.WEB3J_PREFIX;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = WEB3J_PREFIX)
public class Web3Properties {

    public static final String WEB3J_PREFIX = "web3";

    private String clientAddress;

    private Boolean adminClient;
    
    private String networkId;
    
    private String logLevel;
    
    private String solcCmd;

    private Long httpTimeoutSeconds;
    
    private boolean includeRawResponse;

    public boolean isIncludeRawResponse() {
		return includeRawResponse;
	}

	public void setIncludeRawResponse(boolean includeRawResponse) {
		this.includeRawResponse = includeRawResponse;
	}

	public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Boolean getAdminClient() {
		return adminClient;
	}

	public Boolean isAdminClient() {
        return adminClient;
    }

    public void setAdminClient(Boolean adminClient) {
        this.adminClient = adminClient;
    }
    
    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public Long getHttpTimeoutSeconds() {
        return httpTimeoutSeconds;
    }

    public void setHttpTimeoutSeconds(Long httpTimeoutSeconds) {
        this.httpTimeoutSeconds = httpTimeoutSeconds;
    }

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getSolcCmd() {
		return solcCmd;
	}

	public void setSolcCmd(String solcCmd) {
		this.solcCmd = solcCmd;
	}
    
	
}
