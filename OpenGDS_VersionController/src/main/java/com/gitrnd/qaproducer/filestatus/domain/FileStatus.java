package com.gitrnd.qaproducer.filestatus.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileStatus {
	private int fid;
	private String location;
	private String fname;
	private Timestamp ctime;
	private int status;
	private int uidx;
	private String comment;
}
