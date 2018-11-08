package com.gitrnd.gdsbuilder.geogig.command.geoserver;

import java.util.Base64;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;

public class PublishGeoserverLayer {

	private static final String rest = "rest";
	private static final String command_workspaces = "workspaces";
	private static final String command_datastores = "datastores";
	private static final String command_featuretypes = "featuretypes";

	private boolean isSuccess = false;

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
		} catch (HttpClientErrorException e) {
			throw new GeogigCommandException(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (HttpServerErrorException e) {
			throw new GeogigCommandException(e.getResponseBodyAsString(), e.getStatusCode());
		}
		return isSuccess;
	}
}
