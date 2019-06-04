/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig committer xml 응답 객체
 * 
 * @author DY.Oh
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

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	@XmlElement(name = "email")
	public String getEmail() {
		return email;
	}

	@XmlElement(name = "timestamp")
	public String getTimestamp() {
		return timestamp;
	}

	@XmlElement(name = "timeZonOffset")
	public String getTimeZonOffset() {
		return timeZonOffset;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setTimeZonOffset(String timeZonOffset) {
		this.timeZonOffset = timeZonOffset;
	}

}
