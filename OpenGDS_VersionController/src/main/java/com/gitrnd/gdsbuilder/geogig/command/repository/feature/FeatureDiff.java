package com.gitrnd.gdsbuilder.geogig.command.repository.feature;

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
import com.gitrnd.gdsbuilder.geogig.type.GeogigFeatureDiff;

/**
 * Geogig Repository Diff Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class FeatureDiff {

	/**
	 * geogig
	 */
	private static final String geogig = "geogig";
	/**
	 * command
	 */
	private static final String command = "featurediff";
	/**
	 * path parameter
	 */
	private static final String param_path = "path=";
	/**
	 * oldTreeish parameter
	 */
	private static final String param_oldTreeish = "oldTreeish=";
	/**
	 * newTreeish parameter
	 */
	private static final String param_newTreeish = "newTreeish=";
	/**
	 * all parameter (선택)
	 */
	private static final String param_all = "all=";

	/**
	 * 서로 다른 두 버전(CommitID)의 객체의 비교 내용을 반환함.
	 * 
	 * @param baseURL    Geogig Repository가 위치한 Geoserver BaseURL
	 *                   <p>
	 *                   (ex. http://localhost:8080/geoserver)
	 * @param username   Geoserver 사용자 ID
	 * @param password   Geoserver 사용자 PW
	 * @param repository Geogig Repository명
	 * @param path       객체 경로
	 * @param newTreeish 최신 버전의 Commit ID
	 * @param oldTreeish 이전 버전의 Commit ID
	 * @return Command 실행 성공 - 두 Branch의 Merge 목록 또는 UnMerged 목록 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigFeatureDiff executeCommand(String baseURL, String username, String password, String repository,
			String path, String newTreeish, String oldTreeish) {

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
		String url = baseURL + "/" + geogig + "/repos/" + repository + "/" + command + "?" + param_path + path + "&"
				+ param_oldTreeish + oldTreeish + "&" + param_newTreeish + newTreeish + "&" + param_all + "true";

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigFeatureDiff> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigFeatureDiff.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}

}
