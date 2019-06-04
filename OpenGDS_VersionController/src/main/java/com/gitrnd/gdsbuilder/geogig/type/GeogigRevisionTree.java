/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig LsTree Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigRevisionTree {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;

	/**
	 * Node 목록
	 */
	private List<Node> nodes;

	/**
	 * 오류 메세지
	 */
	private String error;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	@XmlElement(name = "node")
	public List<Node> getNodes() {
		return nodes;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Node 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "node")
	public static class Node {

		/**
		 * Node 경로 (Layer 또는 Feature)
		 */
		private String path;

		/**
		 * metadataId
		 */
		private String metadataId;

		/**
		 * type
		 */
		private String type;

		/**
		 * Node Object Id
		 */
		private String objectId;

		@XmlElement(name = "path")
		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		@XmlElement(name = "metadataId")
		public String getMetadataId() {
			return metadataId;
		}

		public void setMetadataId(String metadataId) {
			this.metadataId = metadataId;
		}

		@XmlElement(name = "type")
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		@XmlElement(name = "objectId")
		public String getObjectId() {
			return objectId;
		}

		public void setObjectId(String objectId) {
			this.objectId = objectId;
		}

	}

}
