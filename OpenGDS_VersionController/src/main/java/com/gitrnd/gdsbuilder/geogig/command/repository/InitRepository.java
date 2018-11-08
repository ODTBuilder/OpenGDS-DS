package com.gitrnd.gdsbuilder.geogig.command.repository;

import java.util.Base64;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRepositoryInit;

import net.sf.json.JSONObject;

public class InitRepository {

	private static final Log logger = LogFactory.getLog(InitRepository.class);

	private static final String geogig = "geogig";
	private static final String command = "init";

	public GeogigRepositoryInit executeCommand(String baseURL, String username, String password, String repository,
			String dbHost, String dbPort, String dbName, String dbSchema, String dbUser, String dbPassword,
			String authorName, String authorEmail) {

		// restTemplate
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(5000);
		factory.setConnectTimeout(3000);
		CloseableHttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(100).setMaxConnPerRoute(5).build();
		factory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(factory);

		// header
		HttpHeaders headers = new HttpHeaders();
		String user = username + ":" + password;
		String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(user.getBytes());
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", encodedAuth);

		JSONObject bodyObj = new JSONObject();
		bodyObj.put("dbHost", dbHost);
		bodyObj.put("dbPort", dbPort);
		bodyObj.put("dbName", dbName);
		bodyObj.put("dbSchema", dbSchema);
		bodyObj.put("dbUser", dbUser);
		bodyObj.put("dbPassword", dbPassword);
		bodyObj.put("authorName", authorName);
		bodyObj.put("authorEmail", authorEmail);

		String bodyStr = bodyObj.toString();

		// url
		String url = baseURL + "/" + geogig + "/repos/" + repository + "/" + command;

		HttpEntity<String> requestEntity = new HttpEntity<>(bodyStr, headers);
		ResponseEntity<GeogigRepositoryInit> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, GeogigRepositoryInit.class);
		} catch (HttpClientErrorException e) {
			throw new GeogigCommandException(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (HttpServerErrorException e) {
			throw new GeogigCommandException(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (ResourceAccessException e) {
			throw e;
		}
		return responseEntity.getBody();
	}
}
