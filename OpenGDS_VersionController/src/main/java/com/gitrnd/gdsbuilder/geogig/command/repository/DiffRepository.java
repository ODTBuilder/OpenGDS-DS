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
import com.gitrnd.gdsbuilder.geogig.type.GeogigDiff;

/**
 * Geogig Diff Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class DiffRepository {

	/**
	 * geogig
	 */
	private static final String geogig = "geogig";
	/**
	 * command
	 */
	private static final String command = "diff";
	/**
	 * oldRef parameter, 이전 버전의 Commit ID
	 */
	private static final String param_oldRef = "oldRefSpec=";
	/**
	 * newRef parameter, 최신 버전의 Commit ID
	 */
	private static final String param_newRef = "newRefSpec=";
	/**
	 * pathFilter parameter (선택적), 특정 Branch 또는 Layer 또는 Feature 경로
	 */
	private static final String param_pathFilter = "pathFilter=";
	/**
	 * page parameter (선택적), diff 이력 페이지
	 */
	private static final String param_page = "page=";

	/**
	 * 서로 다른 두 버전(CommitID)의 Geogig Repository의 비교 내용을 반환함.
	 * <p>
	 * pathFilter parameter 입력 시 해당 경로의 객체들만 비교하며 미 입력 시 Repository내에 존재하는 모든 객체를
	 * 비교함.
	 * 
	 * @param baseURL     Geogig Repository가 위치한 Geoserver BaseURL
	 *                    <p>
	 *                    (ex. http://localhost:8080/geoserver)
	 * @param username    Geoserver 사용자 ID
	 * @param password    Geoserver 사용자 PW
	 * @param repository  Geogig Repository명
	 * @param newObjectId 최신 버전의 Commit ID
	 * @param oldObjectId 이전 버전의 Commit ID
	 * @param path        특정 Branch 또는 Layer 또는 Feature 경로
	 * @param page        반환되는 diff 이력 페이지 번호.
	 *                    <p>
	 *                    diff 이력 개수가 30개 이상인 경우 해당.
	 * @return Command 실행 성공 - 두 객체의 비교 결과 목록 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigDiff executeCommand(String baseURL, String username, String password, String repository,
			String newObjectId, String oldObjectId, String path, Integer page) {

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
		String url = baseURL + "/" + geogig + "/repos/" + repository + "/" + command + "?" + param_oldRef + oldObjectId
				+ "&" + param_newRef + newObjectId;

		// path
		if (path != null) {
			url += "&" + param_pathFilter + path;
		}

		// page
		if (page != null) {
			url += "&" + param_page + page;
		}

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigDiff> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigDiff.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}

}
