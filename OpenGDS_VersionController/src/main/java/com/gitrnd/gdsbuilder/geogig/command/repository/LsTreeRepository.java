/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.command.repository;

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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRevisionTree;

/**
 * Geogig Ls-Tree Command Execution Class
 * 
 * @author GIT
 *
 */
public class LsTreeRepository {

	private static final Log logger = LogFactory.getLog(LsTreeRepository.class);

	private static final String geogig = "geogig";
	private static final String command = "ls-tree";
	private static final String param_path = "path="; // optional
	private static final String param_verbose = "verbose="; // optional

	public GeogigRevisionTree executeCommand(String baseURL, String username, String password, String repository,
			String path, boolean verbose) {

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

		if (path != null) {
			if (url.contains("?")) {
				url += "&" + param_path + path;
			} else {
				url += "?" + param_path + path;
			}

		}
		if (verbose == true) {
			if (url.contains("?")) {
				url += "&" + param_verbose + verbose;
			} else {
				url += "?" + param_verbose + verbose;
			}
		}

		// request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<GeogigRevisionTree> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GeogigRevisionTree.class);
		} catch (HttpClientErrorException e) {
			throw new GeogigCommandException(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (HttpServerErrorException e) {
			throw new GeogigCommandException(e.getResponseBodyAsString(), e.getStatusCode());
		}
		return responseEntity.getBody();
	}
}
