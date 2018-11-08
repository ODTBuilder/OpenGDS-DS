/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Branch Command 응답 xml 객체.
 * 
 * @author GIT
 *
 */
@XmlRootElement(name = "response")
public class GeogigBranch {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;
	/**
	 * branch 생성 성공 여부
	 */
	private BranchCreated branchCreated;
	/**
	 * local branch 목록
	 */
	private List<Branch> localBranchList;
	/**
	 * remote branch 목록
	 */
	private List<Branch> remoteBranchList;
	/**
	 * branch 생성 실패
	 */
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
	 * branch 생성 성공 여부 반환
	 * 
	 * @return BranchCreated
	 */
	@XmlElement(name = "BranchCreated")
	public BranchCreated getBranchCreated() {
		return branchCreated;
	}

	/**
	 * local branch 목록 반환
	 * 
	 * @return List<Branch>
	 */
	@XmlElementWrapper(name = "Local")
	@XmlElement(name = "Branch")
	public List<Branch> getLocalBranchList() {
		return localBranchList;
	}

	/**
	 * remote branch 목록 반환
	 * 
	 * @return List<Branch>
	 */
	@XmlElementWrapper(name = "Remote")
	@XmlElement(name = "Branch")
	public List<Branch> getRemoteBranchList() {
		return remoteBranchList;
	}

	/**
	 * @return the error
	 */
	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setBranchCreated(BranchCreated branchCreated) {
		this.branchCreated = branchCreated;
	}

	public void setLocalBranchList(List<Branch> localBranchList) {
		this.localBranchList = localBranchList;
	}

	public void setRemoteBranchList(List<Branch> remoteBranchList) {
		this.remoteBranchList = remoteBranchList;
	}

	/**
	 * BranchCreated 객체
	 * 
	 * @author GIT
	 *
	 */
	@XmlRootElement(name = "BranchCreated")
	public static class BranchCreated {

		/**
		 * branch명
		 */
		private String name;
		/**
		 * branch source
		 */
		private String source;

		/**
		 * branch명 반환
		 * 
		 * @return String
		 */
		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		/**
		 * branch source 반환
		 * 
		 * @return String
		 */
		@XmlElement(name = "source")
		public String getSource() {
			return source;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setSource(String source) {
			this.source = source;
		}

	}

	/**
	 * Branch 객체
	 * 
	 * @author GIT
	 *
	 */
	@XmlRootElement(name = "Branch")
	public static class Branch {

		/**
		 * local branch명
		 */
		private String name;
		/**
		 * remote branch명
		 */
		private String remoteName;
		/**
		 * local branch source
		 */
		private String source;

		/**
		 * local branch명 반환
		 * 
		 * @return String
		 */
		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		/**
		 * remote branch명 반환
		 * 
		 * @return String
		 */
		@XmlElement(name = "remoteName")
		public String getRemoteName() {
			return remoteName;
		}

		/**
		 * local branch source 반환
		 * 
		 * @return String
		 */
		@XmlElement(name = "source")
		public String getSource() {
			return source;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setRemoteName(String remoteName) {
			this.remoteName = remoteName;
		}

		public void setSource(String source) {
			this.source = source;
		}

	}
}
