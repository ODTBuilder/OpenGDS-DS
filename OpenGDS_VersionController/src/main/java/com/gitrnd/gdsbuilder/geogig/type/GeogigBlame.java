package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.gitrnd.gdsbuilder.geogig.type.GeogigRepositoryLog.Commit;

/**
 * Geogig Blame Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigBlame {
	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	/**
	 * 오류 메세지
	 */
	private String error;

	/**
	 * {@link BlameAttribute} 목록
	 */
	private List<BlameAttribute> attributes;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	@XmlElementWrapper(name = "Blame")
	@XmlElement(name = "Attribute")
	public List<BlameAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<BlameAttribute> attributes) {
		this.attributes = attributes;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	/**
	 * BlameAttribute 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "Attribute")
	public static class BlameAttribute {

		/**
		 * 속성명
		 */
		private String name;
		/**
		 * 속성값
		 */
		private String value;
		/**
		 * Commit 버전 목록
		 */
		private List<Commit> commits;

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		@XmlElement(name = "value")
		public String getValue() {
			return value;
		}

		@XmlElement(name = "commit")
		public List<Commit> getCommits() {
			return commits;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public void setCommits(List<Commit> commits) {
			this.commits = commits;
		}

	}

}
