/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.command.repository.remote;

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
import com.gitrnd.gdsbuilder.geogig.type.GeogigRemoteRepository;

/**
 * Geogig Remote Repository Remove Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class RemoveRemoteRepository {

	/**
	 * geogig
	 */
	private static final String geogig = "geogig";
	/**
	 * command
	 */
	private static final String command = "remote";
	/**
	 * remove parameter
	 */
	private static final String param_remove = "remove=";
	/**
	 * remoteName parameter
	 */
	private static final String param_remoteName = "remoteName=";

	/**
	 * 등록된 Remote Repository를 삭제함.
	 * <p>
	 * Remote Repository가 실제 저장소에서 삭제되는 것이 아니며 Remote Repository의 연결을 제거한다는 것을 의미함.
	 * 
	 * @param baseURL    Geogig Repository가 위치한 Geoserver BaseURL
	 *                   <p>
	 *                   (ex. http://localhost:8080/geoserver)
	 * @param username   Geoserver 사용자 ID
	 * @param password   Geoserver 사용자 PW
	 * @param repository Geogig Repository명
	 * @param remoteName Remote Geogig Repository명 (실제 Geogig Repository명이 아닌 등록을 위한
	 *                   별칭을 의미함)
	 * @return Command 실행 성공 - 삭제 성공한 Remote Repository명 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigRemoteRepository executeCommand(String baseURL, String username, String password, String repository,
			String remoteName) {

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
		String url = baseURL + "/" + geogig + "/repos/" + repository + "/" + command + "?" + param_remove + true + "&"
				+ param_remoteName + remoteName;

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigRemoteRepository> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigRemoteRepository.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}
}
