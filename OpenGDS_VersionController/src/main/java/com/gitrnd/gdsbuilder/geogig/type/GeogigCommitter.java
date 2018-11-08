/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig committer xml 응답 객체
 * 
 * @author GIT
 *
 */
@XmlRootElement(name = "committer")
public class GeogigCommitter {

	/**
	 * committer name
	 */
	private String name;
	/**
	 * committer email
	 */
	private String email;
	/**
	 * committer timestamp
	 */
	private String timestamp;
	/**
	 * committer timeZonOffset
	 */
	private String timeZonOffset;

	/**
	 * committer name 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	/**
	 * committer email 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "email")
	public String getEmail() {
		return email;
	}

	/**
	 * committer timestamp 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "timestamp")
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * committer timeZonOffset 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "timeZonOffset")
	public String getTimeZonOffset() {
		return timeZonOffset;
	}

	/**
	 * committer name 설정
	 * 
	 * @param name
	 *            committer name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * committer email 설정
	 * 
	 * @param email
	 *            committer email
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
