package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "workspaces")
public class GeogigGeoserverWorkSpace {

	private String success;

	private String error;

	private List<Workspace> workspaces;

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

	@XmlElement(name = "workspace")
	public List<Workspace> getWorkspaces() {
		return workspaces;
	}

	public void setWorkspaces(List<Workspace> workspaces) {
		this.workspaces = workspaces;
	}

	@XmlRootElement(name = "workspace")
	public static class Workspace {

		private String name;

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
