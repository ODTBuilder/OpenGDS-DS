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
import com.gitrnd.gdsbuilder.geogig.type.GeogigFeatureRevert;

/**
 * Geogig Repository RevertFeature Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class RevertFeature {

	/**
	 * geogig
	 */
	private static final String geogig = "geogig";
	/**
	 * command
	 */
	private static final String command = "revertfeature";
	/**
	 * transactionId parameter
	 */
	private static final String param_transactionId = "transactionId=";
	/**
	 * oldCommitId parameter
	 */
	private static final String param_oldCommitId = "oldCommitId="; // revert commit id
	/**
	 * newCommitId parameter
	 */
	private static final String param_newCommitId = "newCommitId="; // current commit id
	/**
	 * path parameter
	 */
	private static final String param_path = "path=";
	/**
	 * commitMessage parameter
	 */
	private static final String param_commitMessage = "commitMessage=";
	/**
	 * mergeMessage parameter
	 */
	private static final String param_mergeMessage = "mergeMessage=";
	/**
	 * authorName parameter
	 */
	private static final String param_authorName = "authorName=";
	/**
	 * authorEmail parameter
	 */
	private static final String param_authorEmail = "authorEmail=";

	/**
	 * 현재 Checkout한 Branch의 단일 피처에 대해 가장 최근의 Commit ID 버전을 이전 버전의 Commit ID로 되돌림.
	 * <p>
	 * Revert를 수행하면 두 Commit 버전이 병합되어 새로운 Commit ID가 생성됨.
	 * <p>
	 * 병합하는 과정에서 충돌이 발생할 수 있으며 충돌 해결 후 트랜젝션을 종료해야 Geogig Repository Database에 반영됨.
	 * 
	 * @param baseURL       Geogig Repository가 위치한 Geoserver BaseURL
	 *                      <p>
	 *                      (ex. http://localhost:8080/geoserver)
	 * @param username      Geoserver 사용자 ID
	 * @param password      Geoserver 사용자 PW
	 * @param repository    Geogig Repository명
	 * @param transactionId Geogig Repository 트랜잭션 ID
	 * @param oldCommitId   이전 버전의 Commit ID
	 * @param newCommitId   최근 버전의 Commit ID
	 * @param path          객체 경로
	 * @param commitMessage commit 메세지
	 * @param mergeMessage  merge 메세지
	 * @param authorName    사용자명
	 * @param authorEmail   사용자 Email 주소
	 * @return Command 실행 성공 - 두 Branch의 Merge 목록 또는 UnMerged(CONFLIT) 목록 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigFeatureRevert executeCommand(String baseURL, String username, String password, String repository,
			String transactionId, String oldCommitId, String newCommitId, String path, String commitMessage,
			String mergeMessage, String authorName, String authorEmail) {

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
				+ transactionId + "&" + param_oldCommitId + oldCommitId + "&" + param_newCommitId + newCommitId + "&"
				+ param_path + path;

		if (commitMessage != null) {
			url += "&" + param_commitMessage + commitMessage;
		}
		if (mergeMessage != null) {
			url += "&" + param_mergeMessage + mergeMessage;
		}
		if (authorName != null) {
			url += "&" + param_authorName + authorName;
		}
		if (authorEmail != null) {
			url += "&" + param_authorEmail + authorEmail;
		}

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigFeatureRevert> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigFeatureRevert.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}
}
