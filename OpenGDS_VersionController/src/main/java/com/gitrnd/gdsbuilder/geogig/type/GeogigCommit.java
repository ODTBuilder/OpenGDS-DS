package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Commit Command 응답 xml 객체.
 * 
 * @author GIT
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

	private String error;

	/**
	 * Geogig Command 응답 성공 여부 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	/**
	 * commit id 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "commitId")
	public String getCommitId() {
		return commitId;
	}

	/**
	 * 생성 피쳐 개수 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "added")
	public String getAdded() {
		return added;
	}

	/**
	 * 수정 피쳐 개수 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "changed")
	public String getChanged() {
		return changed;
	}

	/**
	 * 삭제 피쳐 개수 반환
	 * 
	 * @return String
	 */
	@XmlElement(name = "deleted")
	public String getDeleted() {
		return deleted;
	}

	/**
	 * Geogig Command 응답 성공 여부 설정
	 * 
	 * @param success Geogig Command 응답 성공 여부
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

	/**
	 * commit id 설정
	 * 
	 * @param commitId commit id
	 */
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	/**
	 * 생성 피쳐 개수 설정
	 * 
	 * @param added 생성 피쳐 개수
	 */
	public void setAdded(String added) {
		this.added = added;
	}

	/**
	 * 수정 피쳐 개수 설정
	 * 
	 * @param changed 수정 피쳐 개수
	 */
	public void setChanged(String changed) {
		this.changed = changed;
	}

	/**
	 * 삭제 피쳐 개수 설정
	 * 
	 * @param deleted 삭제 피쳐 개수
	 */
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
