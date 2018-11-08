/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Geogig author xml 응답 객체
 * 
 * @author GIT
 *
 */
@XmlRootElement(name = "author")
public class GeogigAuthor {

	/**
	 * author name
	 */
	private String name;
	/**
	 * author email
	 */
	private String email;
	/**
	 * author timestamp
	 */
	private String timestamp;
	/**
	 * author timeZonOffset
	 */
	private String timeZonOffset;

	/**
	 * author name 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	/**
	 * author email 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "email")
	public String getEmail() {
		return email;
	}

	/**
	 * author timestamp 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "timestamp")
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * author timeZonOffset 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "timeZonOffset")
	public String getTimeZonOffset() {
		return timeZonOffset;
	}

	/**
	 * author name 설정
	 * 
	 * @param name
	 *            author name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * author email 설정
	 * 
	 * @param email
	 *            author email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * timestamp 설정
	 * 
	 * @param timestamp
	 *            timestamp
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * timeZonOffset 설정
	 * 
	 * @param timeZonOffset
	 *            timeZonOffset
	 */
	public void setTimeZonOffset(String timeZonOffset) {
		this.timeZonOffset = timeZonOffset;
	}

}
