package com.gitrnd.qaproducer.qa.domain;

import java.sql.Timestamp;

import org.json.simple.JSONObject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class ValidationResult extends JSONObject{
	public int pidx;
	public int uidx;
	public int fidx;
	public String zipName;
	public Timestamp createTime;
	public Timestamp endTime;
	public String qaType;
	public String format;
	public int state;
	public String errName;
	public String errFileDir;
	public String comment;
}
