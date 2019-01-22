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
 * @author GIT
 *
 */
@XmlRootElement(name = "response")
public class GeogigCat {

	/**
	 * Geogig Command 응답 성공 여부
	 */
	private String success;
	private Commit commit;
	private Tree tree;
	private Feature feature;
	private FeatureType featuretype;
	private String error;

	/**
	 * @return the success
	 */
	@XmlElement(name = "success")
	public String getSuccess() {
		return success;
	}

	/**
	 * @return the feature
	 */
	@XmlElement(name = "feature")
	public Feature getFeature() {
		return feature;
	}

	/**
	 * @return the commit
	 */
	@XmlElement(name = "commit")
	public Commit getCommit() {
		return commit;
	}

	/**
	 * @return the tree
	 */
	@XmlElement(name = "tree")
	public Tree getTree() {
		return tree;
	}

	/**
	 * @return the featuretype
	 */
	@XmlElement(name = "featuretype")
	public FeatureType getFeaturetype() {
		return featuretype;
	}

	@XmlElement(name = "error")
	public String getError() {
		return error;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(String success) {
		this.success = success;
	}

	/**
	 * @param feature the feature to set
	 */
	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	/**
	 * @param commit the commit to set
	 */
	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	/**
	 * @param tree the tree to set
	 */
	public void setTree(Tree tree) {
		this.tree = tree;
	}

	/**
	 * @param featuretype the featuretype to set
	 */
	public void setFeaturetype(FeatureType featuretype) {
		this.featuretype = featuretype;
	}

	public void setError(String error) {
		this.error = error;
	}

	@XmlRootElement(name = "featuretype")
	public static class FeatureType {

		private String id;
		private String name;
		private List<CatAttribute> attribute;

		/**
		 * @return the id
		 */
		@XmlElement(name = "id")
		public String getId() {
			return id;
		}

		/**
		 * @return the name
		 */
		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		/**
		 * @return the attribute
		 */
		@XmlElement(name = "attribute")
		public List<CatAttribute> getAttribute() {
			return attribute;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @param attribute the attribute to set
		 */
		public void setAttribute(List<CatAttribute> attribute) {
			this.attribute = attribute;
		}

	}

	@XmlRootElement(name = "tree")
	public static class Tree {

		private String id;
		private String size;
		private String numtrees;
		private List<Subtree> subtree;
		private List<Feature> feature;

		/**
		 * @return the id
		 */
		@XmlElement(name = "id")
		public String getId() {
			return id;
		}

		/**
		 * @return the size
		 */
		@XmlElement(name = "size")
		public String getSize() {
			return size;
		}

		/**
		 * @return the numtrees
		 */
		@XmlElement(name = "numtrees")
		public String getNumtrees() {
			return numtrees;
		}

		/**
		 * @return the subtree
		 */
		@XmlElement(name = "subtree")
		public List<Subtree> getSubtree() {
			return subtree;
		}

		/**
		 * @return the feature
		 */
		@XmlElement(name = "feature")
		public List<Feature> getFeature() {
			return feature;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @param size the size to set
		 */
		public void setSize(String size) {
			this.size = size;
		}

		/**
		 * @param numtrees the numtrees to set
		 */
		public void setNumtrees(String numtrees) {
			this.numtrees = numtrees;
		}

		/**
		 * @param subtree the subtree to set
		 */
		public void setSubtree(List<Subtree> subtree) {
			this.subtree = subtree;
		}

		/**
		 * @param feature the feature to set
		 */
		public void setFeature(List<Feature> feature) {
			this.feature = feature;
		}

	}

	@XmlRootElement(name = "subtree")
	public static class Subtree {

		private String name;
		private String type;
		private String objectid;
		private String metadataid;

		/**
		 * @return the name
		 */
		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		/**
		 * @return the type
		 */
		@XmlElement(name = "type")
		public String getType() {
			return type;
		}

		/**
		 * @return the objectid
		 */
		@XmlElement(name = "objectid")
		public String getObjectid() {
			return objectid;
		}

		/**
		 * @return the metadataid
		 */
		@XmlElement(name = "metadataid")
		public String getMetadataid() {
			return metadataid;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @param objectid the objectid to set
		 */
		public void setObjectid(String objectid) {
			this.objectid = objectid;
		}

		/**
		 * @param metadataid the metadataid to set
		 */
		public void setMetadataid(String metadataid) {
			this.metadataid = metadataid;
		}

	}

	@XmlRootElement(name = "commit")
	public static class Commit {

		private String id;
		private String tree;
		private List<String> parentsIds;
		private GeogigCommitter commiter;
		private GeogigAuthor author;

		/**
		 * @return the id
		 */
		@XmlElement(name = "id")
		public String getId() {
			return id;
		}

		/**
		 * @return the tree
		 */
		@XmlElement(name = "tree")
		public String getTree() {
			return tree;
		}

		/**
		 * @return the parentsIds
		 */
		@XmlElementWrapper(name = "parents")
		@XmlElement(name = "id")
		public List<String> getParentsIds() {
			return parentsIds;
		}

		/**
		 * @return the commiter
		 */
		@XmlElement(name = "committer")
		public GeogigCommitter getCommiter() {
			return commiter;
		}

		/**
		 * @return the author
		 */
		@XmlElement(name = "author")
		public GeogigAuthor getAuthor() {
			return author;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @param tree the tree to set
		 */
		public void setTree(String tree) {
			this.tree = tree;
		}

		/**
		 * @param parentsIds the parentsIds to set
		 */
		public void setParentsIds(List<String> parentsIds) {
			this.parentsIds = parentsIds;
		}

		/**
		 * @param commiter the commiter to set
		 */
		public void setCommiter(GeogigCommitter commiter) {
			this.commiter = commiter;
		}

		/**
		 * @param author the author to set
		 */
		public void setAuthor(GeogigAuthor author) {
			this.author = author;
		}

	}

	@XmlRootElement(name = "feature")
	public static class Feature {

		private String id;
		private String name;
		private String type;
		private String objectid;
		private String metadataid;

		private List<CatAttribute> attribute;

		/**
		 * @return the id
		 */
		@XmlElement(name = "id")
		public String getId() {
			return id;
		}

		/**
		 * @return the attribute
		 */
		@XmlElement(name = "attribute")
		public List<CatAttribute> getAttribute() {
			return attribute;
		}

		/**
		 * @return the name
		 */
		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		/**
		 * @return the type
		 */
		@XmlElement(name = "type")
		public String getType() {
			return type;
		}

		/**
		 * @return the objectid
		 */
		@XmlElement(name = "objectid")
		public String getObjectid() {
			return objectid;
		}

		/**
		 * @return the metadataid
		 */
		@XmlElement(name = "metadataid")
		public String getMetadataid() {
			return metadataid;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @param attribute the attribute to set
		 */
		public void setAttribute(List<CatAttribute> attribute) {
			this.attribute = attribute;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @param objectid the objectid to set
		 */
		public void setObjectid(String objectid) {
			this.objectid = objectid;
		}

		/**
		 * @param metadataid the metadataid to set
		 */
		public void setMetadataid(String metadataid) {
			this.metadataid = metadataid;
		}

	}

	@XmlRootElement(name = "attribute")
	public static class CatAttribute {

		private String type;
		private String name;
		private String value;
		private String minoccurs;
		private String maxoccurs;
		private String nillable;
		private String crs;

		/**
		 * @return the type
		 */
		@XmlElement(name = "type")
		public String getType() {
			return type;
		}

		/**
		 * @return the value
		 */
		@XmlElement(name = "value")
		public String getValue() {
			return value;
		}

		/**
		 * @return the minoccurs
		 */
		@XmlElement(name = "minoccurs")
		public String getMinoccurs() {
			return minoccurs;
		}

		/**
		 * @return the maxoccurs
		 */
		@XmlElement(name = "maxoccurs")
		public String getMaxoccurs() {
			return maxoccurs;
		}

		/**
		 * @return the nillable
		 */
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

		/**
		 * @param minoccurs the minoccurs to set
		 */
		public void setMinoccurs(String minoccurs) {
			this.minoccurs = minoccurs;
		}

		/**
		 * @param maxoccurs the maxoccurs to set
		 */
		public void setMaxoccurs(String maxoccurs) {
			this.maxoccurs = maxoccurs;
		}

		/**
		 * @param nillable the nillable to set
		 */
		public void setNillable(String nillable) {
			this.nillable = nillable;
		}

	}

}
