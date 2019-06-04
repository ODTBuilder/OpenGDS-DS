package com.gitrnd.gdsbuilder.geogig.type;

/**
 * Command 실행 성공, 실패 여부 객체.
 * 
 * @author DY.Oh
 *
 */
public class GeogigCommandResponse {

	/**
	 * Command 실행 성공, 실패 여부
	 */
	private String success;

	/**
	 * 오류 메세지
	 */
	private String error;

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

}
