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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;
import com.gitrnd.gdsbuilder.geogig.type.GeogigConfig;

/**
 * Geogig Config Command Execution Class
 * 
 * @author GIT
 *
 */
public class ConfigRepository {

	private static final Log logger = LogFactory.getLog(ConfigRepository.class);

	private static final String geogig = "geogig";
	private static final String command = "config";
	private static final String param_name = "name="; // optional

	public enum ConfigName {

		REPO_NAME("repo.name"), USER_NAME("user.name"), USER_EMAIL("user.email"), STORAGE_REFS("storage.refs"),
		STORAGE_GRAPH("storage.graph"), STORAGE_OBJECTS("storage.objects"), STORAGE_INDEX("storage.index"),
		POSTGRES_VERSION("postgres.version"), ROCKSDB_VERSION("rocksdb.version"), FILE_VERSION("file.version");

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
