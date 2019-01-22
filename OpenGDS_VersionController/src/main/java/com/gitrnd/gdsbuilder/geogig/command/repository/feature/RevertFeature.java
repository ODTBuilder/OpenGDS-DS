package com.gitrnd.gdsbuilder.geogig.command.repository.feature;

import java.util.Base64;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.gitrnd.gdsbuilder.geogig.type.GeogigFeatureRevert;

public class RevertFeature {

	private static final Log logger = LogFactory.getLog(RevertFeature.class);

	private static final String geogig = "geogig";
	private static final String command = "revertfeature";
	private static final String param_transactionId = "transactionId=";
	private static final String param_oldCommitId = "oldCommitId="; // revert commit id
	private static final String param_newCommitId = "newCommitId="; // current commit id
	private static final String param_path = "path=";
	private static final String param_commitMessage = "commitMessage=";
	private static final String param_mergeMessage = "mergeMessage=";
	private static final String param_authorName = "authorName=";
	private static final String param_authorEmail = "authorEmail=";

	public GeogigFeatureRevert executeCommand(String baseURL, String username, String password, String repository,
			String transactionId, String oldCommitId, String newCommitId, String path, String commitMessage,
			String mergeMessage, String authorName, String authorEmail) {

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
				+ transactionId + "&" + param_oldCommitId + oldCommitId + "&" + param_newCommitId + newCommitId + "&"
				+ param_path + path;

		if (commitMessage != null) {
			url += "&" + param_commitMessage + commitMessage;
		}
		if (mergeMessage != null) {
			url += "&" + param_mergeMessage + mergeMessage;
		}
		if (authorName != null) {
			url += "&" + param_authorName + authorName;
		}
		if (authorEmail != null) {
			url += "&" + param_authorEmail + authorEmail;
		}

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigFeatureRevert> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigFeatureRevert.class);
		} catch (RestClientResponseException e) {
			throw new GeogigCommandException(e.getMessage(), e.getResponseBodyAsString(), e.getRawStatusCode());
		} catch (ResourceAccessException e) {
			throw new GeogigCommandException(e.getMessage());
		}
		return responseEntity.getBody();
	}
}
