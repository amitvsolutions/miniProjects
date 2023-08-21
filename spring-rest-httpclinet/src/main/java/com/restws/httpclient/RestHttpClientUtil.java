package com.restws.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import com.restws.common.BaseException;
import com.restws.common.Constants;
import com.restws.common.ErrorConstants;


/**
 * Utility class providing a REST client implementation.
 *
 */
@Component
public class RestHttpClientUtil {

	private static final String DELIMITER = "\n";

	private static final String OTHER_EXCEPTION = "Other Exception";

	private static final String SERVICE_UNAVAILABLE = "Service unavailable.";

	private static final String SERVER_EXCEPTION = "Server Exception.";

	private static final String UNSUPPORTED_MEDIA_TYPE = "Unsupported media type.";

	private static final String PRECONDITION_FAILED = "Precondition failed.";

	private static final String REQUEST_TIME_OUT = "Request time out.";

	private static final String METHOD_NOT_FOUND = "Method not found.";

	private static final String NOT_FOUND_REQUEST = "Not Found. Request: ";

	private static final String FORBIDDEN = "Forbidden.";

	private static final String UNAUTHORIZED = "Unauthorized.";

	private static final String BAD_REQUEST = "Bad Request.";

	private static final String REQUEST_ACCEPTED = "Request accepted.";

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RestHttpClientUtil.class);

	/** The is prop loaded. */
	private boolean isPropLoaded = false;

	/** The connReqTimeout. */
	private int connReqTimeout = 30000;

	/** The connTimeout. */
	private int connTimeout = 30000;

	/** The socketTimeout. */
	private int socketTimeout = 20000;

	/** The maxRedirect. */
	private int maxRedirect = 2;

	/** The connectionMaxTotal. */
	private int connectionMaxTotal = 100;

	/** The defaultMaxRoute. */
	private int defaultMaxRoute = 50;

	/** The httpclient. */
	private CloseableHttpClient httpclient = null;


	/** The cm. */
	private static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

	private enum HTTPMethod {

		/** The get. */
		GET,
		/** The post. */
		POST,
		/** The put. */
		PUT
	}

	@PostConstruct
	public void init() {
		if (!isPropLoaded) {
			try {
				//connReqTimeout = Integer.parseInt(Constants.HTTP_CONN_REQ_TIMEOUT);
				//connTimeout = Integer.parseInt(Constants.HTTP_CONN_TIMEOUT);
				//socketTimeout = Integer.parseInt(Constants.HTTP_SOCKET_TIMEOUT);
				//maxRedirect = Integer.parseInt(Constants.HTTP_MAX_REDIRECT);
				//connectionMaxTotal = Integer.parseInt(Constants.HTTP_CONN_MAX_TOTAL);
				//defaultMaxRoute = Integer.parseInt(Constants.HTTP_DEFAULT_MAX_ROUTE);
				isPropLoaded = true;
			} catch (Exception e) {
				LOGGER.error("REST Client Exception", e);
			}
			/*
			 * setMaxTotal(int max): Set the maximum number of total open connections.
			 * setDefaultMaxPerRoute(int max): Set the maximum number of concurrent
			 * connections per route, which is 2 by default. setMaxPerRoute(int max): Set
			 * the total number of concurrent connections to a specific route, which is 2 by
			 * default.
			 */
			// TO DO - to be parameterized
			cm.setMaxTotal(connectionMaxTotal);
			cm.setDefaultMaxPerRoute(defaultMaxRoute);
		}
	}

	/**
	 * Gets the HTTP client.
	 *
	 * @return the HTTP client
	 */
	private void getHTTPClient() {
		LOGGER.info("Entering getHTTPClient with param at {}", System.currentTimeMillis());

		try {
			if (httpclient == null) {
				SocketConfig sconfig = SocketConfig.custom().setSoKeepAlive(Boolean.FALSE).build();
				httpclient = HttpClients.custom().setDefaultSocketConfig(sconfig).setConnectionManager(cm).build();
			}
		} catch (Exception e) {
			throw new BaseException("Failed :HttpClient", e);
		} finally {
			LOGGER.info("Exiting getHTTPClient at {}", System.currentTimeMillis());
		}
	}

	public String executeGetRequest(String url, Map<String, String> header, int connReqTimeout, int connTimeout,
			int socketTimeout) {
		LOGGER.info("Entering executeGetRequest at {}", System.currentTimeMillis());
		HttpGet request = null;
		try {
			getHTTPClient();
			request = (HttpGet) setHttpHeaderNConfig(header, getHTTPGetRequest(url), connReqTimeout, connTimeout,
					socketTimeout);
			return executeAndSendHttpResponse(url, request);
		} finally {
			closeHTTPRequest(request);
			LOGGER.info("Exiting executeGetRequest at {}", System.currentTimeMillis());
		}
	}

	public String executeGetRequest(String url, Map<String, String> header) {
		return executeGetRequest(url, header, connReqTimeout, connTimeout, socketTimeout);
	}

	public String executePostRequest(String url, Object json, Map<String, String> header, int connReqTimeout,
			int connTimeout, int socketTimeout) throws UnsupportedEncodingException {
		return executePostRequest(url, json, header, connReqTimeout, connTimeout, socketTimeout, 0);
	}

	public String executePostRequest(String url, Object json, Map<String, String> header, int connReqTimeout,
			int connTimeout, int socketTimeout, int retryCount) throws UnsupportedEncodingException {
		LOGGER.info("Entering executePostRequest at {}", System.currentTimeMillis());
		HttpPost request = null;
		try {
			getHTTPClient();
			StringEntity params = null;
			params = new StringEntity(json.toString());
			request = (HttpPost) setHttpHeaderNConfig(header, getHTTPPostRequest(url), connReqTimeout, connTimeout,
					socketTimeout);
			if (null != request)
				request.setEntity(params);
			return executeAndSendHttpResponse(url, request, retryCount);
		} finally {
			closeHTTPRequest(request);// closing the request
			LOGGER.info("Exiting executePostRequest at {}", System.currentTimeMillis());
		}
	}

	public String executePostRequest(String url, Object json, Map<String, String> header)
			throws UnsupportedEncodingException {
		return executePostRequest(url, json, header, 0);
	}

	public String executePostRequest(String url, Object json, Map<String, String> header, int retryCount)
			throws UnsupportedEncodingException {
		return executePostRequest(url, json, header, connReqTimeout, connTimeout, socketTimeout, retryCount);
	}

	public String executePutRequest(String url, Object json, Map<String, String> header, int connReqTimeout,
			int connTimeout, int socketTimeout) throws UnsupportedEncodingException {
		LOGGER.info("Entering executePutRequest at {}", System.currentTimeMillis());
		HttpPut request = null;
		try {
			getHTTPClient();
			StringEntity params = null;
			params = new StringEntity(json.toString());
			request = (HttpPut) setHttpHeaderNConfig(header, getHTTPPutRequest(url), connReqTimeout, connTimeout,
					socketTimeout);
			if (null != request)
				request.setEntity(params);
			return executeAndSendHttpResponse(url, request);
		} finally {
			closeHTTPRequest(request);// closing the request
			LOGGER.info("Exiting executePutRequest at {}", System.currentTimeMillis());
		}
	}

	public String executePutRequest(String url, Object json, Map<String, String> header)
			throws UnsupportedEncodingException {
		return executePutRequest(url, json, header, connReqTimeout, connTimeout, socketTimeout);
	}

	private HttpRequestBase getHTTPRequest(String restUri, Enum<HTTPMethod> httpMethod) {
		HttpRequestBase request = null;
		if (restUri != null) {
			if (httpMethod.name().equals(Constants.HTTP_METHOD_POST)) {
				request = new HttpPost(restUri);
			} else if (httpMethod.name().equals(Constants.HTTP_METHOD_GET)) {
				request = new HttpGet(restUri);
			} else if (httpMethod.name().equals(Constants.HTTP_METHOD_PUT)) {
				request = new HttpPut(restUri);
			}
		}
		return request;
	}

	private HttpGet getHTTPGetRequest(String restUri) {
		return (HttpGet) getHTTPRequest(restUri, HTTPMethod.GET);
	}

	private HttpPut getHTTPPutRequest(String restUri) {
		return (HttpPut) getHTTPRequest(restUri, HTTPMethod.PUT);
	}

	private HttpPost getHTTPPostRequest(String restUri) {
		return (HttpPost) getHTTPRequest(restUri, HTTPMethod.POST);
	}

	private HttpRequestBase setHttpHeaderNConfig(Map<String, String> headerMap, HttpRequestBase request,
			int connReqTimeout, int connTimeout, int socketTimeout) {
		headerMap.forEach(request::addHeader);
		if (null != request) {
			request.setConfig(RequestConfig.custom().setConnectionRequestTimeout(connReqTimeout)
					.setConnectTimeout(connTimeout).setSocketTimeout(socketTimeout)
					.setExpectContinueEnabled(Boolean.TRUE).setMaxRedirects(maxRedirect).build());
		}
		return request;
	}

	private String executeAndSendHttpResponse(String url, HttpRequestBase request) {
		return executeAndSendHttpResponse(url, request, 0);
	}

	private String executeAndSendHttpResponse(String url, HttpRequestBase request, int retryCount) {
		LOGGER.info("Entering executeAndSendHttpResponse at {}", System.currentTimeMillis());
		CloseableHttpResponse response = null;
		long startTime = System.currentTimeMillis();
		try {
			response = httpclient.execute(request);
			if (response.getStatusLine() != null) {
				if (response.getStatusLine().getStatusCode() != 200) {
					logError(request, response);
					throw handleHttpResponseStatus(response, request);
				}
				return read(response.getEntity().getContent());
			}
			return null;
		} catch (SocketTimeoutException ste) {
			if (retryCount > 0) {
				LOGGER.error(
						"REST Client SocketTimeoutException. Will attempt retry for url {}. Time taken for first retry {}. Retries Available: {} - Request: {} - Config: {}. Exception : {}",
						url, System.currentTimeMillis() - startTime, retryCount, request, request.getConfig(), ste);
				return executeAndSendHttpResponse(url, request, retryCount - 1);
			} else {
				LOGGER.error("REST Client SocketTimeoutException.  Url {}, Time taken {}, Request: {}. Exception {}",
						url, System.currentTimeMillis() - startTime, request, ste);
				throw new BaseException(restMessage("REST Client SocketTimeoutException. ", request), ste);
			}
		} catch (NoHttpResponseException e) {
			LOGGER.error("REST Client NoHttpResponseException. Time taken {}. Request: {}. Exception {}",
					System.currentTimeMillis() - startTime, request, e);
			return executeAndSendHttpResponse(url, request, retryCount-1);
		} catch (IOException e) {
			throw new BaseException(restMessage("REST Client IOException. ", request), e);
		} finally {
			closeHTTPRequest(request);// closing the request
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
				} catch (Exception e) {
					LOGGER.error("Exception during response closing in" + " executeAndSendHttpResponse {}", e);
				}
			}
			LOGGER.info("Exiting executeAndSendHttpResponse {}", System.currentTimeMillis());
		}
	}

	private BaseException handleHttpResponseStatus(HttpResponse response, HttpRequestBase request) {
		BaseException exp = null;
		switch (response.getStatusLine().getStatusCode()) {
		case 202:
			exp = new BaseException(ErrorConstants.RC_202, restMessage(REQUEST_ACCEPTED, request));
			break;
		case 400:
			exp = new BaseException(ErrorConstants.RC_400, restMessage(BAD_REQUEST, request));
			break;
		case 401:
			exp = new BaseException(ErrorConstants.RC_401, restMessage(UNAUTHORIZED, request));
			break;
		case 403:
			exp = new BaseException(ErrorConstants.RC_403, restMessage(FORBIDDEN, request));
			break;
		case 404:
			exp = new BaseException(ErrorConstants.RC_404, restMessage(NOT_FOUND_REQUEST, request));
			break;
		case 405:
			exp = new BaseException(ErrorConstants.RC_405, restMessage(METHOD_NOT_FOUND, request));
			break;
		case 408:
			exp = new BaseException(ErrorConstants.RC_408, restMessage(REQUEST_TIME_OUT, request));
			break;
		case 412:
			exp = new BaseException(ErrorConstants.RC_412, restMessage(PRECONDITION_FAILED, request));
			break;
		case 415:
			exp = new BaseException(ErrorConstants.RC_415, restMessage(UNSUPPORTED_MEDIA_TYPE, request));
			break;
		case 500:
			exp = new BaseException(ErrorConstants.RC_500, restMessage(SERVER_EXCEPTION, request));
			break;
		case 503:
			exp = new BaseException(ErrorConstants.RC_503, restMessage(SERVICE_UNAVAILABLE, request));
			break;
		default:
			exp = new BaseException(ErrorConstants.RC_DEFAULT, restMessage(OTHER_EXCEPTION, request));
			break;
		}
		return exp;

	}

	private String restMessage(String reason, HttpRequestBase request) {
		return reason + " Request: " + request;

	}

	private void closeHTTPRequest(HttpRequestBase httpRequest) {
		if (null != httpRequest) {
			httpRequest.releaseConnection();
		}
	}

	private static String read(InputStream input) throws IOException {
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
			return buffer.lines().collect(Collectors.joining(DELIMITER));
		}
	}

	public Object[] executeGetBytes(String url, Map<String, String> header, int connReqTimeout, int connTimeout,
			int socketTimeout) {
		LOGGER.info("Entering executeGetRequest at {}", System.currentTimeMillis());
		HttpGet request = null;
		try {
			getHTTPClient();
			request = (HttpGet) setHttpHeaderNConfig(header, getHTTPGetRequest(url), connReqTimeout, connTimeout,
					socketTimeout);
			return executeAndSendHttpBytes(url, request);
		} finally {
			closeHTTPRequest(request);
			LOGGER.info("Exiting executeGetRequest at {}", System.currentTimeMillis());
		}
	}

	public Object[] executePostBytes(String url, Object json, Map<String, String> header, int connReqTimeout,
			int connTimeout, int socketTimeout) throws UnsupportedEncodingException {
		LOGGER.info("Entering executePostBytes at {}", System.currentTimeMillis());
		HttpPost request = null;
		try {
			getHTTPClient();
			StringEntity params = null;
			params = new StringEntity(json.toString());
			request = (HttpPost) setHttpHeaderNConfig(header, getHTTPPostRequest(url), connReqTimeout, connTimeout,
					socketTimeout);
			if (null != request)
				request.setEntity(params);
			return executeAndSendHttpBytes(url, request);
		} finally {
			closeHTTPRequest(request);
			LOGGER.info("Exiting executePostBytes at {}", System.currentTimeMillis());
		}
	}

	public Object[] executeGetBytes(String url, Map<String, String> header) {
		return executeGetBytes(url, header, connReqTimeout, connTimeout, socketTimeout);
	}

	public Object[] executePostBytes(String url, Object json, Map<String, String> header)
			throws UnsupportedEncodingException {
		return executePostBytes(url, json, header, connReqTimeout, connTimeout, socketTimeout);
	}

	private Object[] executeAndSendHttpBytes(String url, HttpRequestBase request) {
		return executeAndSendHttpBytes(url, request, 0);
	}

	private Object[] executeAndSendHttpBytes(String url, HttpRequestBase request, int retryCount) {
		LOGGER.info("Entering executeAndSendHttpResponse at {}", System.currentTimeMillis());
		CloseableHttpResponse response = null;
		Map<String, String> headerMap = null;
		long startTime = System.currentTimeMillis();
		try {
			response = httpclient.execute(request);
			if (response.getStatusLine() != null) {
				if (response.getStatusLine().getStatusCode() != 200) {
					logError(request, response);
					throw handleHttpResponseStatus(response, request);
				}
				headerMap = new HashMap<>();
				for (Header header : response.getAllHeaders()) {
					headerMap.put(header.getName(), header.getValue());
				}
				return new Object[] { headerMap, IOUtils.toByteArray(response.getEntity().getContent()) };
			}
			return new Object[] {};
		} catch (SocketTimeoutException ste) {
			if (retryCount > 0) {
				LOGGER.error(
						"REST Client SocketTimeoutException. Will attempt retry for url {}. Time taken for first retry {}. Retries Available: {} - Request: {} - Config: {}. Exception : {}",
						url, System.currentTimeMillis() - startTime, retryCount, request, request.getConfig(), ste);
				return executeAndSendHttpBytes(url, request, retryCount - 1);
			} else {
				LOGGER.error("REST Client SocketTimeoutException.  Url {}, Time taken {}, Request: {}. Exception {}",
						url, System.currentTimeMillis() - startTime, request, ste);
				throw new BaseException(restMessage("REST Client SocketTimeoutException. ", request), ste);
			}
		} catch (NoHttpResponseException e) {
			LOGGER.error("REST Client NoHttpResponseException. Time taken {}. Request: {}. Exception {}",
					System.currentTimeMillis() - startTime, request, e);
			return executeAndSendHttpBytes(url, request, retryCount-1);
		} catch (IOException e) {
			throw new BaseException(restMessage("REST Client IOException. ", request), e);
		} finally {
			closeHTTPRequest(request);// closing the request
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
				} catch (Exception e) {
					LOGGER.error("Exception during response closing in" + " executeAndSendHttpResponse {}", e);
				}
			}
			LOGGER.info("Exiting executeAndSendHttpResponse {}", System.currentTimeMillis());
		}
	}

	private void logError(HttpRequestBase request, CloseableHttpResponse response) throws IOException {
		/**
		 * it is suggested to read the response content to flush the thread back to
		 * pool. Here we are reading and also logging request / response details in log
		 **/
		LOGGER.error(" Error Code : {}{}{}", (response.getStatusLine().getStatusCode()), request,
				null != response.getEntity() ? read(response.getEntity().getContent()) : response);
	}
	
	private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() 
	{
	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
	    clientHttpRequestFactory.setConnectionRequestTimeout(connReqTimeout); // Connection Request timeout
	    clientHttpRequestFactory.setConnectTimeout(connTimeout); // Connect timeout
	    clientHttpRequestFactory.setReadTimeout(socketTimeout); //Read(Socket) timeout
	    //SocketConfig.Builder.setSoTimeout(socketTimeout);
	    return clientHttpRequestFactory;
	}
	
	public ResponseEntity<byte[]> executeGetBytes(HttpHeaders headers, String url, Object ...uriVariables)
			throws HttpClientErrorException, HttpServerErrorException, UnknownHttpStatusCodeException {
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		return restTemplate.exchange(url, HttpMethod.GET, requestEntity, byte[].class, uriVariables);
	}

	public ResponseEntity<String> executePostRequest(String serverUrl, HttpEntity<MultiValueMap<String, Object>> requestEntity)
			throws HttpClientErrorException, HttpServerErrorException, UnknownHttpStatusCodeException {
		RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		return restTemplate.postForEntity(serverUrl, requestEntity, String.class);
	}

	public ResponseEntity<byte[]> executePostBytes(String serverUrl, HttpEntity<String> requestEntity)
			throws HttpClientErrorException, HttpServerErrorException, UnknownHttpStatusCodeException {
		RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		return restTemplate.postForEntity(serverUrl, requestEntity, byte[].class);
	}
	
	public String executePostMultiPartRequest(String url, org.apache.http.HttpEntity mainEntity , Map<String, String> header, int retryCount) throws UnsupportedEncodingException {
		LOGGER.info("Entering executePostMultiPartRequest at {}", System.currentTimeMillis());
		HttpPost request = null;
		try {
			getHTTPClient();
			request = (HttpPost) setHttpHeaderNConfig(header, getHTTPPostRequest(url), connReqTimeout, connTimeout,
					socketTimeout);
			if (null != request)
				request.setEntity(mainEntity);
			return executeAndSendHttpResponse(url, request, retryCount);
		} finally {
			closeHTTPRequest(request);// closing the request
			LOGGER.info("Exiting executePostMultiPartRequest at {}", System.currentTimeMillis());
		}
	}
	
}

