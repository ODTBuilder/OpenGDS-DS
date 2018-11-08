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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;
import com.gitrnd.gdsbuilder.geogig.command.ResponseType;
import com.gitrnd.gdsbuilder.geogig.type.GeogigGeoserverLayerList;

public class ListGeoserverLayer {

	private static final String rest = "rest";
	private static final String command_workspaces = "workspaces";
	private static final String command_datastores = "datastores";
	private static final String command_featuretypes = "featuretypes";
	private static final String command_list = "list=";

	public enum ListParam {

		CONFIGURED, AVAILABLE, ALL;

	}

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
		} catch (HttpClientErrorException e) {
			throw new GeogigCommandException(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (HttpServerErrorException e) {
			throw new GeogigCommandException(e.getResponseBodyAsString(), e.getStatusCode());
		}
		return responseEntity.getBody();
	}

}
