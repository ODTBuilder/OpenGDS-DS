package com.gitrnd.gdsbuilder.geogig.type;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geoserver Workspace 목록 조회 결과 객체.
 * 
 * @author DY.Oh
 *
 */
@XmlRootElement(name = "workspaces")
public class GeogigGeoserverWorkSpaceList {

	/**
	 * Command 응답 성공 여부
	 */
	private String success;

	/**
	 * error message
	 */
	private String error;

	/**
	 * Geoserver Workspace 목록
	 */
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

	/**
	 * Workspace 객체.
	 * 
	 * @author DY.Oh
	 *
	 */
	@XmlRootElement(name = "workspace")
	public static class Workspace {

		/**
		 * Workspace명
		 */
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
