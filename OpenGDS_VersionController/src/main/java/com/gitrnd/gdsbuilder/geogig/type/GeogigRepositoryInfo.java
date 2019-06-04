package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Repository Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "repository")
public class GeogigRepositoryInfo {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;
	/**
	 * error message
	 */
	private String error;
	/**
	 * Repository ID
	 */
	private String id;
	/**
	 * Repository명
	 */
	private String name;
	/**
	 * Repository 주소
	 */
	private String url;
	/**
	 * Repository 위치
	 */
	private String location;
	/**
	 * Repository 저장소 타입
	 */
	private String storage;
	/**
	 * 사용자명
	 */
	private String user;
	/**
	 * 사용자 Email 주소
	 */
	private String email;

	@XmlElement(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "location")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
