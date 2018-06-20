package com.hpb.web3.protocol.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpb.web3.protocol.Service;
import com.hpb.web3.protocol.exceptions.ClientConnectionException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;

public class HttpService extends Service {

	public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

	public static final String DEFAULT_URL = "http://localhost:8545/";
	private static final Logger log = LoggerFactory.getLogger(HttpService.class);

	private OkHttpClient httpClient;

	private final String url;

	private final boolean includeRawResponse;

	private HashMap<String, String> headers = new HashMap<>();

	public HttpService(String url, OkHttpClient httpClient, boolean includeRawResponses) {
		super(includeRawResponses);
		this.url = url;
		this.httpClient = httpClient;
		this.includeRawResponse = includeRawResponses;
	}

	public HttpService(OkHttpClient httpClient, boolean includeRawResponses) {
		this(DEFAULT_URL, httpClient, includeRawResponses);
	}

	private HttpService(String url, OkHttpClient httpClient) {
		this(url, httpClient, false);
	}

	public HttpService(String url) {
		this(url, createOkHttpClient());
	}

	public HttpService(String url, boolean includeRawResponse) {
		this(url, createOkHttpClient(), includeRawResponse);
	}

	public HttpService(OkHttpClient httpClient) {
		this(DEFAULT_URL, httpClient);
	}

	public HttpService(boolean includeRawResponse) {
		this(DEFAULT_URL, includeRawResponse);
	}

	public HttpService() {
		this(DEFAULT_URL);
	}

	private static OkHttpClient createOkHttpClient() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		configureLogging(builder);
		return builder.build();
	}

	private static void configureLogging(OkHttpClient.Builder builder) {
		if (log.isDebugEnabled()) {
			HttpLoggingInterceptor logging = new HttpLoggingInterceptor(log::debug);
			logging.setLevel(HttpLoggingInterceptor.Level.BODY);
			builder.addInterceptor(logging);
		}
	}

	private static final int MAX = 5;
	private static final int TIME_OUT = 18000;

	public HttpClient httpClient() {
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(MAX);
		connectionManager.setDefaultMaxPerRoute(MAX);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT)
				.setConnectionRequestTimeout(TIME_OUT).build();
		return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager)
				.build();
	}

	@Override
	protected InputStream performIO(String request) throws IOException {

		/*
		 * HttpClient client=httpClient(); HttpPost httpPost = new HttpPost(url);
		 * httpPost.setHeader("Content-type", "application/json"); StringEntity s = new
		 * StringEntity(request,"UTF-8"); s.setContentType("application/json");
		 * httpPost.setEntity(s); HttpResponse response = client.execute(httpPost); int
		 * statusCode = response.getStatusLine().getStatusCode(); return
		 * response.getEntity().getContent();
		 */
		RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, request);
		Headers headers = buildHeaders();

		okhttp3.Request httpRequest = new okhttp3.Request.Builder().url(url).headers(headers).post(requestBody).build();

		okhttp3.Response response = httpClient.newCall(httpRequest).execute();
		if (response.isSuccessful()) {
			ResponseBody responseBody = response.body();
			if (responseBody != null) {
				return buildInputStream(responseBody);
			} else {
				return null;
			}
		} else {
			throw new ClientConnectionException("Invalid response received: " + response.body());
		}
	}

	private InputStream buildInputStream(ResponseBody responseBody) throws IOException {
		InputStream inputStream = responseBody.byteStream();

		if (includeRawResponse) {

			BufferedSource source = responseBody.source();
			source.request(Long.MAX_VALUE);
			Buffer buffer = source.buffer();

			long size = buffer.size();
			if (size > Integer.MAX_VALUE) {
				throw new UnsupportedOperationException("Non-integer input buffer size specified: " + size);
			}

			int bufferSize = (int) size;
			BufferedInputStream bufferedinputStream = new BufferedInputStream(inputStream, bufferSize);

			bufferedinputStream.mark(inputStream.available());
			return bufferedinputStream;

		} else {
			return inputStream;
		}
	}

	private Headers buildHeaders() {
		return Headers.of(headers);
	}

	public void addHeader(String key, String value) {
		headers.put(key, value);
	}

	public void addHeaders(Map<String, String> headersToAdd) {
		headers.putAll(headersToAdd);
	}

	public HashMap<String, String> getHeaders() {
		return headers;
	}
}
