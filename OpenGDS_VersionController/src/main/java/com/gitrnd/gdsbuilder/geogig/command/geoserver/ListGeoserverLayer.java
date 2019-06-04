package com.gitrnd.gdsbuilder.geogig.command.geoserver;

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
import com.gitrnd.gdsbuilder.geogig.command.ResponseType;
import com.gitrnd.gdsbuilder.geogig.type.GeogigGeoserverLayerList;

/**
 * Geoserver Layer List GET Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class ListGeoserverLayer {

	/**
	 * rest
	 */
	private static final String rest = "rest";
	/**
	 * workspaces parameter
	 */
	private static final String command_workspaces = "workspaces";
	/**
	 * datastores parameter
	 */
	private static final String command_datastores = "datastores";
	/**
	 * featuretypes parameter
	 */
	private static final String command_featuretypes = "featuretypes";
	/**
	 * list parameter
	 */
	private static final String command_list = "list=";

	/**
	 * DataStore Layer 목록 조회 방식 enum
	 * 
	 * @author DY.Oh
	 *
	 */
	public enum ListParam {

		/**
		 * Geoserver 발행 Layer 목록 조회
		 */
		CONFIGURED,
		/**
		 * Geoserver 미발행 Layer 목록 조회
		 */
		AVAILABLE,
		/**
		 * Geoserver 발행, 미발행 Layer 목록 조회
		 */
		ALL;

	}

	/**
	 * DataStore에 존재하는 모든 Layer 정보 목록을 반환함.
	 * 
	 * @param baseURL   Geoserver BaseURL
	 *                  <p>
	 *                  (ex. http://localhost:8080/geoserver)
	 * @param username  Geoserver 사용자 ID
	 * @param password  Geoserver 사용자 PW
	 * @param workspace Geoserver workspace명
	 * @param datasotre Geoserver datastore명
	 * @param type      응답 타입(xml 또는 json)
	 * @param listParam 조회 형태
	 * @return Command 실행 성공 - datastore에 존재하는 모든 Layer 정보 목록 반환
	 *         <p>
	 *         Command 실행 실패 - error 반환
	 * 
	 * @author DY.Oh
	 */
	public GeogigGeoserverLayerList executeCommand(String baseURL, String username, String password, String workspace,
			String datasotre, ResponseType type, ListParam listParam) {

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
		String url = baseURL + "/" + rest + "/" + command_workspaces + "/" + workspace + "/" + command_datastores + "/"
				+ datasotre + "/" + command_featuretypes + "." + type;

		if (listParam != null) {
			url += "?" + command_list + listParam;
		}

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigGeoserverLayerList> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigGeoserverLayerList.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}

}
