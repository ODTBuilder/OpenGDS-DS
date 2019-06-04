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
import com.gitrnd.gdsbuilder.geogig.type.GeogigConfig;

/**
 * Geogig Config Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class ConfigRepository {

	/**
	 * geogig
	 */
	private static final String geogig = "geogig";
	/**
	 * command
	 */
	private static final String command = "config";
	/**
	 * name parameter (선택적), Geogig Repository의 특정 속성명
	 */
	private static final String param_name = "name=";

	/**
	 * Geogig Repository 속성명 enum 클래스.
	 * 
	 * @author DY.Oh
	 *
	 */
	public enum ConfigName {

		/**
		 * Repository명 속성명
		 */
		REPO_NAME("repo.name"),
		/**
		 * 사용자명 속성명
		 */
		USER_NAME("user.name"),
		/**
		 * 사용자 메일 속성명
		 */
		USER_EMAIL("user.email"),
		/**
		 * 저장소 레퍼런스 속성명
		 */
		STORAGE_REFS("storage.refs"), STORAGE_GRAPH("storage.graph"), STORAGE_OBJECTS("storage.objects"),
		STORAGE_INDEX("storage.index"), POSTGRES_VERSION("postgres.version"), ROCKSDB_VERSION("rocksdb.version"),
		FILE_VERSION("file.version");

		private String type;

		private ConfigName(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

	/**
	 * Geogig Repository의 설정 정보를 조회함.
	 * <p>
	 * {@link ConfigName}에 해당하는 설정 중 모든 설정 정보를 리스트 형태로 반환하거나 특정 설정 정보만 반환할 수 있음.
	 * 
	 * @param baseURL     Geogig Repository가 위치한 Geoserver BaseURL
	 *                    <p>
	 *                    (ex. http://localhost:8080/geoserver)
	 * @param username    Geoserver 사용자 ID
	 * @param password    Geoserver 사용자 PW
	 * @param repository  Geogig Repository명
	 * @param storageRefs Geogig Repository 설정 정보의 컬럼명
	 *                    <p>
	 *                    해당 컬럼에 해당하는 정보를 조회할 때 입력.
	 *                    <p>
	 *                    {@code null} 입력 시 모든 설정 정보 반환.
	 * @return Command 실행 성공 - Geogig Repository의 설정 정보 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigConfig executeCommand(String baseURL, String username, String password, String repository,
			ConfigName storageRefs) {

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

		if (storageRefs != null) {
			url += "?" + param_name + storageRefs.getType();
		}
		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigConfig> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigConfig.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}
}
