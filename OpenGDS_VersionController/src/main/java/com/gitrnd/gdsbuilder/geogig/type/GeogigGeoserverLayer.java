package com.gitrnd.gdsbuilder.geogig.type;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "layer")
public class GeogigGeoserverLayer {

	private String success;

	private String error;

	private String name;

	private String type;

	private DefaultStyle defaultStyle;

	private Resource resouce;

	private Attribution attribution;

	private Identifiers identifiers;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "defaultStyle")
	public DefaultStyle getDefaultStyle() {
		return defaultStyle;
	}

	public void setDefaultStyle(DefaultStyle defaultStyle) {
		this.defaultStyle = defaultStyle;
	}

	@XmlElement(name = "resource")
	public Resource getResouce() {
		return resouce;
	}

	public void setResouce(Resource resouce) {
		this.resouce = resouce;
	}

	@XmlElement(name = "attribution")
	public Attribution getAttribution() {
		return attribution;
	}

	public void setAttribution(Attribution attribution) {
		this.attribution = attribution;
	}

	@XmlElement(name = "identifiers")
	public Identifiers getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(Identifiers identifiers) {
		this.identifiers = identifiers;
	}

	@XmlRootElement(name = "defaultStyle")
	public static class DefaultStyle {

		private String name;

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	@XmlRootElement(name = "resouce")
	public static class Resource {

		private String resouceClass;

		private String name;

		@XmlAttribute
		public String getResouceClass() {
			return resouceClass;
		}

		public void setResouceClass(String resouceClass) {
			this.resouceClass = resouceClass;
		}

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	@XmlRootElement(name = "attribution")
	public static class Attribution {

		private String logoWidth;

		private String logoHeight;

		@XmlElement(name = "logoWidth")
		public String getLogoWidth() {
			return logoWidth;
		}

		public void setLogoWidth(String logoWidth) {
			this.logoWidth = logoWidth;
		}

		@XmlElement(name = "logoHeight")
		public String getLogoHeight() {
			return logoHeight;
		}

		public void setLogoHeight(String logoHeight) {
			this.logoHeight = logoHeight;
		}
	}

	@XmlRootElement(name = "identifiers")
	public static class Identifiers {

		private Identifier identifier;

		@XmlElement(name = "Identifier")
		public Identifier getIdentifier() {
			return identifier;
		}

		public void setIdentifier(Identifier identifier) {
			this.identifier = identifier;
		}

	}

	@XmlRootElement(name = "Identifier")
	public static class Identifier {

		private String authority;

		private String identifier;

		@XmlElement(name = "authority")
		public String getAuthority() {
			return authority;
		}

		public void setAuthority(String authority) {
			this.authority = authority;
		}

		@XmlElement(name = "identifier")
		public String getIdentifier() {
			return identifier;
		}

		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}

	}
}
