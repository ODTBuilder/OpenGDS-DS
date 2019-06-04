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
import com.gitrnd.gdsbuilder.geogig.type.GeogigPull;

/**
 * Geogig Repository Pull Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class PullRepository {

	/**
	 * geogig
	 */
	private static final String geogig = "geogig";
	/**
	 * command
	 */
	private static final String command = "pull";
	/**
	 * transactionId parameter
	 */
	private static final String param_transactionId = "transactionId=";
	/**
	 * remoteName parameter
	 */
	private static final String param_remoteName = "remoteName=";
	/**
	 * ref parameter
	 */
	private static final String param_ref = "ref=";
	/**
	 * authorName parameter
	 */
	private static final String param_authorName = "authorName="; // optional
	/**
	 * authorEmail parameter
	 */
	private static final String param_authorEmail = "authorEmail="; // optional

	/**
	 * Remote Repository로 등록된 Geogig Repository의 추가, 삭제, 수정의 변동사항들을 현재 Checkout 상태인
	 * Repository의 Branch에 병합함.
	 * <p>
	 * 각 Branch의 수정 사항이 충돌(UnMerged 상태)이 발생할 경우 Command 실행은 성공되나 충돌 목록(객체별 두
	 * Repository의 Commit ID)이 반환됨.
	 * <p>
	 * 충돌이 발생하지 않으면 두 Branch가 Merge 상태가 됨.
	 * 
	 * @param baseURL          Geogig Repository가 위치한 Geoserver BaseURL
	 *                         <p>
	 *                         (ex. http://localhost:8080/geoserver)
	 * @param username         Geoserver 사용자 ID
	 * @param password         Geoserver 사용자 PW
	 * @param repository       Geogig Repository명
	 * @param transactionId    Geogig Repository 트랜잭션 ID
	 *                         <p>
	 *                         해당 트랜잭선 ID가 존재하지 않는 경우 Command 실행 실패
	 * @param remoteName       Remote Geogig Repository명
	 * @param branchName       Geogig Repository의 Branch명
	 * @param remoteBranchName Remote Geogig Repository의 Branch명
	 * @param authorName       사용자명
	 * @param authorEmail      사용자 Email 주소
	 * @return Command 실행 성공 - 두 Branch의 Merge 목록 또는 UnMerged 목록 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigPull executeCommand(String baseURL, String username, String password, String repository,
			String transactionId, String remoteName, String branchName, String remoteBranchName, String authorName,
			String authorEmail) {

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
				+ transactionId + "&" + param_remoteName + remoteName + "&" + param_ref + branchName + ":"
				+ remoteBranchName;

		if (authorName != null) {
			url += "&" + param_authorName + authorName;
		}
		if (authorEmail != null) {
			url += "&" + param_authorEmail + authorEmail;
		}

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigPull> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigPull.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}

}
