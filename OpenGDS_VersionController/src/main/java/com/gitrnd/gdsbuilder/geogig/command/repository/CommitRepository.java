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
import com.gitrnd.gdsbuilder.geogig.type.GeogigCommit;

/**
 * Geogig Commit Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class CommitRepository {

	/**
	 * geogig
	 */
	private static final String geogig = "geogig";
	/**
	 * command
	 */
	private static final String command = "commit";
	/**
	 * transactionId parameter
	 */
	private static final String param_transactionId = "transactionId=";
	/**
	 * message parameter (선택적)
	 */
	private static final String param_message = "message="; // optional
	/**
	 * authorName parameter (선택적)
	 */
	private static final String param_authorName = "authorName="; // optional
	/**
	 * authorEmail parameter (선택적)
	 */
	private static final String param_authorEmail = "authorEmail="; // optional

	/**
	 * Staging Area에 Add 되어 있는 수정 사항을 Geogig Repository Database에 반영함.
	 * <p>
	 * 현재 Checkout 상태인 Branch의 Transaction에 반영됨. Branch의 Status가 unMerged(Conflict)인
	 * 경우 반영되지 않으며 Commit 후 Transaction을 종료해야 최종 반영됨.
	 * 
	 * @param baseURL       Geogig Repository가 위치한 Geoserver BaseURL
	 *                      <p>
	 *                      (ex. http://localhost:8080/geoserver)
	 * @param username      Geoserver 사용자 ID
	 * @param password      Geoserver 사용자 PW
	 * @param repository    Geogig Repository명
	 * @param transactionId Geogig Repository 트랜잭션 ID
	 * @param message       Commit Message
	 * @param authorName    사용자 이름
	 * @param authorEmail   사용자 Email 주소
	 * @return Command 실행 성공 - Commit ID 및 추가, 변경 및 삭제된 객체 수 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigCommit executeCommand(String baseURL, String username, String password, String repository,
			String transactionId, String message, String authorName, String authorEmail) {

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
		String url = baseURL + "/" + geogig + "/repos/" + repository + "/" + command + "?" + param_transactionId
				+ transactionId;

		// optional param
		if (message != null) {
			url += "&" + param_message + message;
		}
		if (authorName != null) {
			url += "&" + param_authorName + authorName;
		}
		if (authorEmail != null) {
			url += "&" + param_authorEmail + authorEmail;
		}

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigCommit> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigCommit.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}
}
