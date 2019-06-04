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
import com.gitrnd.gdsbuilder.geogig.type.GeogigRepositoryLog;

/**
 * Geogig Log Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class LogRepository {

	/**
	 * geogig
	 */
	private static final String geogig = "geogig";
	/**
	 * command
	 */
	private static final String command = "log";
	/**
	 * path parameter (선택적), Geogig Repository, Layer, Feature의 경로
	 */
	private static final String param_path = "path="; // optional
	/**
	 * limit parameter (선택적), Geogig Commit 이력 개수 지정
	 */
	private static final String param_limit = "limit="; // optional
	/**
	 * until parameter (선택적), Geogig Commit 이력 목록의 마지막 인덱스
	 */
	private static final String param_until = "until="; // optional
	/**
	 * countChanges parameter (선택적), Geogig Commit 변화 이력 조회 여부
	 */
	private static final String param_countChanges = "countChanges="; // optional

	/**
	 * Geogig Repository의 Master Branch의 Commit 이력 목록 반환.
	 * <p>
	 * Master Branch의 변경 사항을 Repository Database에 저장 및 반영한 이력을 Commit 이력이라 함.
	 * 
	 * @param baseURL      Geogig Repository가 위치한 Geoserver BaseURL
	 *                     <p>
	 *                     (ex. http://localhost:8080/geoserver)
	 * @param username     Geoserver 사용자 ID
	 * @param password     Geoserver 사용자 PW
	 * @param repository   Geogig Repository명
	 * @param path         Geogig Repository, Layer, Feature의 경로
	 *                     <p>
	 *                     Geogig Repository 경로 입력 시 Geogig Repository Master
	 *                     Branch의 모든 레이어의 Commit 이력 조회
	 *                     <p>
	 *                     Layer 경로 입력 시 해당 레이어의 Commit 이력만 조회하며 Feature 경로 입력 시 해당
	 *                     Feature의 Commit 이력만 조회
	 * @param limit        Geogig Commit 이력 개수
	 * @param until        Geogig Commit 이력 목록의 마지막 인덱스
	 * @param countChanges Geogig Commit 변화 이력 조회 여부
	 * @return Command 실행 성공 - Geogig Commit 목록 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigRepositoryLog executeCommand(String baseURL, String username, String password, String repository,
			String path, String limit, String until, boolean countChanges) {

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
		if (limit != null) {
			if (url.contains("?")) {
				url += "&" + param_limit + limit;
			} else {
				url += "?" + param_limit + limit;
			}
		}
		if (until != null) {
			if (url.contains("?")) {
				url += "&" + param_until + until;
			} else {
				url += "?" + param_until + until;
			}
		}
		if (countChanges == true) {
			if (url.contains("?")) {
				url += "&" + param_countChanges + countChanges;
			} else {
				url += "?" + param_countChanges + countChanges;
			}
		}

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigRepositoryLog> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigRepositoryLog.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}
}
