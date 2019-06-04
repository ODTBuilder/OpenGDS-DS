/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.command.repository.branch;

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
import com.gitrnd.gdsbuilder.geogig.type.GeogigMerge;

/**
 * Geogig Branch Merge Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class MergeBranch {

	/**
	 * geogig
	 */
	private static final String geogig = "geogig";
	/**
	 * command
	 */
	private static final String command = "merge";
	/**
	 * transactionId parameter
	 */
	private static final String param_transactionId = "transactionId=";
	/**
	 * commit parameter
	 */
	private static final String param_commit = "commit=";

	/**
	 * 동일한 Geogig Repository 내에 존재하는 두 Branch를 병함함.
	 * <p>
	 * 병합은 특정 Branch의 데이터로 덮어 쓰는 의미가 아니며 현재 Checkout 상태인 Branch에 병합하고자 하는 Branch의
	 * 데이터를 반영하는 의미임.
	 * <p>
	 * 동일한 데이터에 대해 수정했을 경우 충돌이 발생하며 병합은 실패함. 충돌 해결 후 다시 병합을 요청해야함.
	 * 
	 * @param baseURL       Geogig Repository가 위치한 Geoserver BaseURL
	 *                      <p>
	 *                      (ex. http://localhost:8080/geoserver)
	 * @param username      Geoserver 사용자 ID
	 * @param password      Geoserver 사용자 PW
	 * @param repository    Geogig Repository명
	 * @param transactionId Geogig Repository 트랜잭션 ID
	 * @param branchName    병합하고자 하는 Branch명
	 * @return Command 실행 성공 - 두 Branch의 Merge 성공 이력 또는 충돌 이력 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigMerge executeCommand(String baseURL, String username, String password, String repository,
			String transactionId, String branchName) {

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
				+ transactionId + "&" + param_commit + branchName;

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigMerge> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigMerge.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}
}
