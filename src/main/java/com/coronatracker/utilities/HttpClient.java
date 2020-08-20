package com.coronatracker.utilities;

import java.lang.reflect.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpClient {

	@Value("${spring.ct.httpClient.connectionTimeout}")
	private int connectionTimeout;
	
	@Value("${spring.ct.httpClient.readTimout}")
	private int readTimeOut;
	
	private Logger logger = LogManager.getLogger(getClass());
	
	private RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

	public <T, V> ResponseEntity<T> executeRequest(String url, Type type, HttpMethod httpMethod, HttpEntity<V> body,
			String... queryParams) {
		try {
			ResponseEntity<T> response = restTemplate.exchange(url, httpMethod, body,
					ParameterizedTypeReference.forType(type));
			return response;
		} catch (Exception exception) {
			logger.error("HttpClient: exception while fetching response for url "+url+" message: "+  exception.getMessage());
			throw exception;
		}
	}

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

		requestFactory.setConnectTimeout(connectionTimeout);
		requestFactory.setConnectTimeout(readTimeOut);

		return requestFactory;
	}
}