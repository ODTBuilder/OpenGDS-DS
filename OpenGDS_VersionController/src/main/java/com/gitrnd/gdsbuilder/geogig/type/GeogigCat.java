/**
 * 
 */
package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geogig Cat Feature Command 응답 xml 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "response")
public class GeogigCat {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;
	/**
	 * Commit 정보
	 */
	private Commit commit;
	/**
	 * Tree 정보
	 */
	private Tree tree;
	/**
	 * Feature 정보
	 */
	private Feature feature;
	/**
	 * FeatureType 정보
	 */
	private FeatureType featuretype;
	/**
	 * 오류 메시지
	 */
	private String error;

	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	@XmlElement(name = "feature")
	public Feature getFeature() {
		return feature;
	}

	@XmlElement(name = "commit")
	public Commit getCommit() {
		return commit;
	}

	@XmlElement(name = "tree")
	public Tree getTree() {
		return tree;
	}

	@XmlElement(name = "featuretype")
	public FeatureType getFeaturetype() {
		return featuretype;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public void setFeaturetype(FeatureType featuretype) {
		this.featuretype = featuretype;
	}

	public void setError(String error) {
		this.error = error;
	}

	/**
	 * FeatureType 정보
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "featuretype")
	public static class FeatureType {

		/**
		 * FeatureType Object ID
		 */
		private String id;
		/**
		 * FeatureType Name
		 */
		private String name;
		/**
		 * 속성 컬럼명 목록
		 */
		private List<CatAttribute> attribute;

		@XmlElement(name = "id")
		public String getId() {
			return id;
		}

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		@XmlElement(name = "attribute")
		public List<CatAttribute> getAttribute() {
			return attribute;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setAttribute(List<CatAttribute> attribute) {
			this.attribute = attribute;
		}

	}

	/**
	 * Tree 정보
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "tree")
	public static class Tree {

		/**
		 * Tree ID
		 */
		private String id;
		/**
		 * Sizs
		 */
		private String size;
		/**
		 * Sub Tree 개수
		 */
		private String numtrees;
		/**
		 * Sub Tree 목록
		 */
		private List<Subtree> subtree;
		/**
		 * Feature 목록
		 */
		private List<Feature> feature;

		@XmlElement(name = "id")
		public String getId() {
			return id;
		}

		@XmlElement(name = "size")
		public String getSize() {
			return size;
		}

		@XmlElement(name = "numtrees")
		public String getNumtrees() {
			return numtrees;
		}

		@XmlElement(name = "subtree")
		public List<Subtree> getSubtree() {
			return subtree;
		}

		@XmlElement(name = "feature")
		public List<Feature> getFeature() {
			return feature;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public void setNumtrees(String numtrees) {
			this.numtrees = numtrees;
		}

		public void setSubtree(List<Subtree> subtree) {
			this.subtree = subtree;
		}

		public void setFeature(List<Feature> feature) {
			this.feature = feature;
		}

	}

	/**
	 * Subtree(Layer) 정보
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "subtree")
	public static class Subtree {

		/**
		 * layer명
		 */
		private String name;
		/**
		 * type(TREE)
		 */
		private String type;
		/**
		 * objectid
		 */
		private String objectid;
		/**
		 * metadataid
		 */
		private String metadataid;

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		@XmlElement(name = "type")
		public String getType() {
			return type;
		}

		@XmlElement(name = "objectid")
		public String getObjectid() {
			return objectid;
		}

		@XmlElement(name = "metadataid")
		public String getMetadataid() {
			return metadataid;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setType(String type) {
			this.type = type;
		}

		public void setObjectid(String objectid) {
			this.objectid = objectid;
		}

		public void setMetadataid(String metadataid) {
			this.metadataid = metadataid;
		}

	}

	/**
	 * Commit 정보
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "commit")
	public static class Commit {

		/**
		 * Commit ID
		 */
		private String id;
		/**
		 * Tree ID
		 */
		private String tree;
		/**
		 * 상위 버전 Commit ID
		 */
		private List<String> parentsIds;
		/**
		 * Commit한 사용자 정보
		 */
		private GeogigCommitter commiter;
		/**
		 * Geogig Repository 권한자 정보
		 */
		private GeogigAuthor author;

		@XmlElement(name = "id")
		public String getId() {
			return id;
		}

		@XmlElement(name = "tree")
		public String getTree() {
			return tree;
		}

		@XmlElementWrapper(name = "parents")
		@XmlElement(name = "id")
		public List<String> getParentsIds() {
			return parentsIds;
		}

		@XmlElement(name = "committer")
		public GeogigCommitter getCommiter() {
			return commiter;
		}

		@XmlElement(name = "author")
		public GeogigAuthor getAuthor() {
			return author;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setTree(String tree) {
			this.tree = tree;
		}

		public void setParentsIds(List<String> parentsIds) {
			this.parentsIds = parentsIds;
		}

		public void setCommiter(GeogigCommitter commiter) {
			this.commiter = commiter;
		}

		public void setAuthor(GeogigAuthor author) {
			this.author = author;
		}

	}

	/**
	 * Feature 정보
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "feature")
	public static class Feature {

		/**
		 * Feature Object ID
		 */
		private String id;
		/**
		 * Feature name
		 */
		private String name;
		/**
		 * type
		 */
		private String type;
		/**
		 * objectid
		 */
		private String objectid;
		/**
		 * metadataid
		 */
		private String metadataid;
		/**
		 * 속성 목록
		 */
		private List<CatAttribute> attribute;

		@XmlElement(name = "id")
		public String getId() {
			return id;
		}

		@XmlElement(name = "attribute")
		public List<CatAttribute> getAttribute() {
			return attribute;
		}

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		@XmlElement(name = "type")
		public String getType() {
			return type;
		}

		@XmlElement(name = "objectid")
		public String getObjectid() {
			return objectid;
		}

		@XmlElement(name = "metadataid")
		public String getMetadataid() {
			return metadataid;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setAttribute(List<CatAttribute> attribute) {
			this.attribute = attribute;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setType(String type) {
			this.type = type;
		}

		public void setObjectid(String objectid) {
			this.objectid = objectid;
		}

		public void setMetadataid(String metadataid) {
			this.metadataid = metadataid;
		}

	}

	/**
	 * Feature Attribute 정보
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "attribute")
	public static class CatAttribute {

		/**
		 * 속성 타입
		 */
		private String type;
		/**
		 * 속성명
		 */
		private String name;
		/**
		 * 속성값
		 */
		private String value;
		/**
		 * 
		 */
		private String minoccurs;
		/**
		 * 
		 */
		private String maxoccurs;
		/**
		 * 
		 */
		private String nillable;
		/**
		 * 좌표계 (ex.EPSG:4326)
		 */
		private String crs;

		/**
		 * @return the type
		 */
		@XmlElement(name = "type")
		public String getType() {
			return type;
		}

		@XmlElement(name = "value")
		public String getValue() {
			return value;
		}

		@XmlElement(name = "minoccurs")
		public String getMinoccurs() {
			return minoccurs;
		}

		@XmlElement(name = "maxoccurs")
		public String getMaxoccurs() {
			return maxoccurs;
		}

		@XmlElement(name = "nillable")
		public String getNillable() {
			return nillable;
		}

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		@XmlElement(name = "crs")
		public String getCrs() {
			return crs;
		}

		public void setCrs(String crs) {
			this.crs = crs;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setType(String type) {
			this.type = type;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public void setMinoccurs(String minoccurs) {
			this.minoccurs = minoccurs;
		}

		public void setMaxoccurs(String maxoccurs) {
			this.maxoccurs = maxoccurs;
		}

		public void setNillable(String nillable) {
			this.nillable = nillable;
		}

	}

}
