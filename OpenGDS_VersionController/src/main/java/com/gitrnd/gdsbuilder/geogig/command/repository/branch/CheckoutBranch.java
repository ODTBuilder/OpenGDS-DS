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
import com.gitrnd.gdsbuilder.geogig.type.GeogigCheckout;

/**
 * Geogig Branch Checkout Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class CheckoutBranch {

	/**
	 * geogig
	 */
	private static final String geogig = "geogig";
	/**
	 * command
	 */
	private static final String command = "checkout";
	/**
	 * transactionId parameter
	 */
	private static final String param_transactionId = "transactionId=";
	/**
	 * branch parameter
	 */
	private static final String param_branch = "branch=";
	/**
	 * path parameter
	 */
	private static final String param_path = "path=";
	/**
	 * ours parameter
	 */
	private static final String param_ours = "ours=";
	/**
	 * theirs parameter
	 */
	private static final String param_theirs = "theirs=";
	/**
	 * oursparameter
	 */
	public static final String CHEKCOUT_OURS = "ours";
	/**
	 * theirs parameter
	 */
	public static final String CHEKCOUT_THEIRS = "theirs";

	/**
	 * 특정 Branch를 수정하기 위해 현재 Branch에서 수정하고자 하는 Branch로 이동함.
	 * 
	 * @param baseURL       Geogig Repository가 위치한 Geoserver BaseURL
	 *                      <p>
	 *                      (ex. http://localhost:8080/geoserver)
	 * @param username      Geoserver 사용자 ID
	 * @param password      Geoserver 사용자 PW
	 * @param repository    Geogig Repository명
	 * @param transactionId Geogig Repository 트랜잭션 ID
	 *                      <p>
	 *                      해당 트랜잭선 ID가 존재하지 않는 경우 Command 실행 실패
	 * @param branchName    Checkout Branch명
	 * @return Command 실행 성공 - Checkout 전,후의 Branch명 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigCheckout executeCommand(String baseURL, String username, String password, String repository,
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
				+ transactionId + "&" + param_branch + branchName;

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigCheckout> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigCheckout.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}

	/**
	 * 두 Branch 병합시 발생한 충돌을 특정 Branch의 객체로 덮어씌워 충돌을 해결함.
	 * 
	 * @param baseURL       Geogig Repository가 위치한 Geoserver BaseURL
	 *                      <p>
	 *                      (ex. http://localhost:8080/geoserver)
	 * @param username      Geoserver 사용자 ID
	 * @param password      Geoserver 사용자 PW
	 * @param repository    Geogig Repository명
	 * @param transactionId Geogig Repository 트랜잭션 ID
	 *                      <p>
	 *                      해당 트랜잭선 ID가 존재하지 않는 경우 Command 실행 실패
	 * @param path          현재 Checkout된 Branch의 충돌이 발생한 객체의 경로
	 * @param version       "ours" 또는 "theirs" 문자열만 입력 가능
	 *                      <p>
	 *                      "ours" : 현재 Checkout된 Branch의 객체로 덮어씌움
	 *                      <p>
	 *                      "theirs" : 다른 Branch의 객체로 현재 현재 Checkout된 Branch의 객체를
	 *                      덮어씌움
	 * @return Command 실행 성공 - Checkout을 성공한 객체의 경로와 version("ours" 또는 "theirs") 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigCheckout executeCommand(String baseURL, String username, String password, String repository,
			String transactionId, String path, String version) {

		// restTemplate
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(5000);
		factory.setConnectTimeout(3000);

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
				+ transactionId + "&" + param_path + path;

		if (version.equalsIgnoreCase(CHEKCOUT_OURS)) {
			url += "&" + param_ours + true;
		} else if (version.equalsIgnoreCase(CHEKCOUT_THEIRS)) {
			url += "&" + param_theirs + true;
		}

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigCheckout> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigCheckout.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}
}
