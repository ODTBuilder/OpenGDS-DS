/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.command.transaction;

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
import com.gitrnd.gdsbuilder.geogig.type.GeogigTransaction;

/**
 * Geogig CancelTransaction Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class CancelTransaction {

	/**
	 * geogig
	 */
	private static final String geogig = "geogig";
	/**
	 * command
	 */
	private static final String command = "endTransaction";
	/**
	 * transactionId Parameter
	 */
	private static final String param_transactionId = "transactionId=";
	/**
	 * cancel Parameter
	 */
	private static final String param_cancel = "cancel=true";

	/**
	 * 생성한 Geogig Repository 트랜잭션을 취소함.
	 * <P>
	 * Geogig Repository를 수정하기 위해 생성하였던 트랜잭션을 취소하며 해당 Geogig Repository의 수정사항을 모두
	 * 폐기함.
	 * 
	 * @param baseURL       Geogig Repository가 위치한 Geoserver BaseURL
	 *                      <p>
	 *                      (ex. http://localhost:8080/geoserver)
	 * @param username      Geoserver 사용자 ID
	 * @param password      Geoserver 사용자 PW
	 * @param repository    Geogig Repository명
	 * @param transactionId 생성한 Geogig Repository 트랜잭션 ID
	 *                      <p>
	 *                      해당 트랜잭선 ID가 존재하지 않는 경우 Command 실행 실패
	 * @return Command 실행 성공 - 트랜잭선 ID 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigTransaction executeCommand(String baseURL, String username, String password, String repository,
			String transactionId) {

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
				+ transactionId + "&" + param_cancel;

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigTransaction> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigTransaction.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}
}
