/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.command.repository;

import java.util.Base64;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRevisionTree;

/**
 * Geogig Ls-Tree Command 실행 클래스
 * 
 * @author DY.Oh
 */
public class LsTreeRepository {

	/**
	 * geogig
	 */
	private static final String geogig = "geogig";
	/**
	 * command
	 */
	private static final String command = "ls-tree";
	/**
	 * path parameter (선택적), Geogig Repository의 Branch명 또는 Commit ID
	 */
	private static final String param_path = "path=";
	/**
	 * verbose parameter (선택적), 각 노드의 유형, medatadata ID 및 오브젝트 ID 반환 여부
	 */
	private static final String param_verbose = "verbose=";

	/**
	 * Geogig Repository의 특정 Branch 또는 Commit의 Layer 목록을 조회함.
	 * 
	 * @param baseURL    Geogig Repository가 위치한 Geoserver BaseURL
	 *                   <p>
	 *                   (ex. http://localhost:8080/geoserver)
	 * @param username   Geoserver 사용자 ID
	 * @param password   Geoserver 사용자 PW
	 * @param repository Geogig Repository명
	 * @param path       Geogig Repository의 Branch명 또는 Commit ID
	 * @param verbose    "true" - 각 노드의 유형, medatadata ID 및 오브젝트 ID를 포함하여 반환
	 * @return Command 실행 성공 - Layer 목록 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigRevisionTree executeCommand(String baseURL, String username, String password, String repository,
			String path, boolean verbose) {

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
		headers.setContentType(MediaType.APPLICATION_XML);
		headers.add("Authorization", encodedAuth);

		// url
		String url = baseURL + "/" + geogig + "/repos/" + repository + "/" + command;

		if (path != null) {
			if (url.contains("?")) {
				url += "&" + param_path + path;
			} else {
				url += "?" + param_path + path;
			}

		}
		if (verbose == true) {
			if (url.contains("?")) {
				url += "&" + param_verbose + verbose;
			} else {
				url += "?" + param_verbose + verbose;
			}
		}

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigRevisionTree> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigRevisionTree.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}
}
