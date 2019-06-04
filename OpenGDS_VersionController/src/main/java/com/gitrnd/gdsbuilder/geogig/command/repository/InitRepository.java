package com.gitrnd.gdsbuilder.geogig.command.repository;

import java.util.Base64;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRepositoryInit;

/**
 * Geogig InitRepository Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class InitRepository {

	/**
	 * geogig parameter
	 */
	private static final String geogig = "geogig";
	/**
	 * command parameter
	 */
	private static final String command = "init";

	/**
	 * Geoserver REST API를 통해 Geogig Repository 생성.
	 * <p>
	 * Geogig Repository는 Geoserver에 설치된 Geogig Plugin을 통해 PostGIS Geogig 저장소를 생성함.
	 * <p>
	 * 동일한 Geoserver내에는 Geogig Repository명이 중복될 수 없음.
	 * 
	 * @param baseURL     Geogig Repository가 위치한 Geoserver BaseURL
	 *                    <p>
	 *                    (ex.http://localhost:8080/geoserver)
	 * @param username    Geoserver 사용자 ID
	 * @param password    Geoserver 사용자 PW
	 * @param repository  생성하고자 하는 Geogig Repository명
	 * @param dbHost      PostGIS dbHost
	 *                    <p>
	 *                    (ex.localhost)
	 * @param dbPort      PostGIS dbPort
	 *                    <p>
	 *                    (ex.5432)
	 * @param dbName      PostGIS dbName
	 * @param dbSchema    PostGIS dbSchema
	 *                    <p>
	 *                    (ex.public)
	 * @param dbUser      PostGIS dbUser
	 *                    <p>
	 *                    (ex.postgres)
	 * @param dbPassword  PostGIS dbPassword
	 * @param authorName  Geogig Repository 생성 사용자명
	 * @param authorEmail Geogig Repository 생성 사용자 Email 주소
	 * @return Command 실행 성공 - Geogig Repository ID 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
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
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}
}
