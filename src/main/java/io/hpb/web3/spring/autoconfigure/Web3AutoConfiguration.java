package io.hpb.web3.spring.autoconfigure;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.Web3Service;
import io.hpb.web3.protocol.admin.Admin;
import io.hpb.web3.protocol.http.HttpService;
import io.hpb.web3.protocol.ipc.UnixIpcService;
import io.hpb.web3.protocol.ipc.WindowsIpcService;
import io.hpb.web3.spring.actuate.Web3HealthIndicator;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


@Configuration
@ConditionalOnClass(Web3.class)
@EnableConfigurationProperties(Web3Properties.class)
public class Web3AutoConfiguration {

    private static Log log = LogFactory.getLog(Web3AutoConfiguration.class);

    @Autowired
    private Web3Properties properties;

    @Bean
    @ConditionalOnMissingBean
    public Web3 web3() {
        Web3Service web3Service = buildService(properties.getClientAddress());
        log.info("Building service for endpoint: " + properties.getClientAddress());
        return Web3.build(web3Service);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = Web3Properties.WEB3J_PREFIX, name = "admin-client", havingValue = "true")
    public Admin admin() {
        Web3Service web3Service = buildService(properties.getClientAddress());
        log.info("Building admin service for endpoint: " + properties.getClientAddress());
        return Admin.build(web3Service);
    }

    private Web3Service buildService(String clientAddress) {
        Web3Service web3Service;

        if (clientAddress == null || clientAddress.equals("")) {
            web3Service = new HttpService(createOkHttpClient());
        } else if (clientAddress.startsWith("http")) {
            web3Service = new HttpService(clientAddress, createOkHttpClient(), false);
        } else if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            web3Service = new WindowsIpcService(clientAddress,properties.isIncludeRawResponse(),properties.getLogLevel());
        } else {
            web3Service = new UnixIpcService(clientAddress,properties.isIncludeRawResponse(),properties.getLogLevel());
        }

        return web3Service;
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        configureLogging(builder);
        configureTimeouts(builder);
        return builder.build();
    }

    private void configureTimeouts(OkHttpClient.Builder builder) {
        Long tos = properties.getHttpTimeoutSeconds();
        if (tos != null) {
            builder.connectTimeout(tos, TimeUnit.SECONDS);
            builder.readTimeout(tos, TimeUnit.SECONDS);  
            builder.writeTimeout(tos, TimeUnit.SECONDS);
        }
    }

    private void configureLogging(OkHttpClient.Builder builder) {
        if (log.isDebugEnabled()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(log::debug);
            String logLevel = properties.getLogLevel();
            if("NONE".equalsIgnoreCase(logLevel)) {
            	logging.setLevel(HttpLoggingInterceptor.Level.NONE);
            }else if("BASIC".equalsIgnoreCase(logLevel)) {
            	logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            }else if("HEADERS".equalsIgnoreCase(logLevel)) {
            	logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            }else {
            	logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            }
            builder.addInterceptor(logging);
        }
    }


    @Bean
    @ConditionalOnBean(Web3.class)
    Web3HealthIndicator web3HealthIndicator(Web3 web3) {
        return new Web3HealthIndicator(web3);
    }
}
