package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Commit Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigCommit {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;
	/**
	 * commit id
	 */
	private String commitId;
	/**
	 * 생성 피쳐 개수
	 */
	private String added;
	/**
	 * 수정 피쳐 개수
	 */
	private String changed;
	/**
	 * 삭제 피쳐 개수
	 */
	private String deleted;

	/**
	 * 오류 메세지
	 */
	private String error;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	@XmlElement(name = "commitId")
	public String getCommitId() {
		return commitId;
	}

	@XmlElement(name = "added")
	public String getAdded() {
		return added;
	}

	@XmlElement(name = "changed")
	public String getChanged() {
		return changed;
	}

	@XmlElement(name = "deleted")
	public String getDeleted() {
		return deleted;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public void setAdded(String added) {
		this.added = added;
	}

	public void setChanged(String changed) {
		this.changed = changed;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
