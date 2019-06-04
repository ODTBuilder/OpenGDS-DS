package com.gitrnd.gdsbuilder.geogig.command.geoserver;

import java.util.Base64;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;

/**
 * Geoserver Layer Publish POST Command 실행 클래스.
 * 
 * @author DY.Oh
 *
 */
public class PublishGeoserverLayer {

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
	 * isSuccess. 요청 성공 여부
	 */
	private boolean isSuccess = false;

	/**
	 * Geoserver Workspace에 해당 Layer 발행.
	 * 
	 * @param baseURL   Geoserver BaseURL
	 *                  <p>
	 *                  (ex. http://localhost:8080/geoserver)
	 * @param username  Geoserver 사용자 ID
	 * @param password  Geoserver 사용자 PW
	 * @param workspace Geoserver workspace명
	 * @param datastore datastore workspace명
	 * @param layerName Geoserver layer명
	 * @param src       좌표계 (ex.EPSG:4326)
	 * @param enabled   Geoserver 발행 여부
	 * @return 요청 성공 여부
	 * 
	 * @author DY.Oh
	 */
	public boolean executeCommand(String baseURL, String username, String password, String workspace, String datastore,
			String layerName, String src, boolean enabled) {

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
				+ datastore + "/" + command_featuretypes;

		String featureTypeStr = "<featureType><name>" + layerName + "</name><nativeCRS>" + src + "</nativeCRS><enabled>"
				+ enabled + "</enabled></featureType>";

		// request
		HttpEntity<String> requestEntity = new HttpEntity<>(featureTypeStr, headers);
		try {
			restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			isSuccess = true;
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return isSuccess;
	}
}
