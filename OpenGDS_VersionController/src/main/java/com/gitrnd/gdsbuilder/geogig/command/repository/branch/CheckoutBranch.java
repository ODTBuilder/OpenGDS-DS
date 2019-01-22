/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.command.repository.branch;

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
import com.gitrnd.gdsbuilder.geogig.type.GeogigCheckout;

/**
 * Geogig Branch Checkout Command Execution Class
 * 
 * @author GIT
 *
 */
public class CheckoutBranch {

	private static final Log logger = LogFactory.getLog(CheckoutBranch.class);

	private static final String geogig = "geogig";
	private static final String command = "checkout";
	private static final String param_transactionId = "transactionId=";
	private static final String param_branch = "branch=";
	private static final String param_path = "path=";
	private static final String param_ours = "ours=";
	private static final String param_theirs = "theirs=";

	public static final String CHEKCOUT_OURS = "ours";
	public static final String CHEKCOUT_THEIRS = "theirs";

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
