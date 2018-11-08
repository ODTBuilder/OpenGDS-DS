/**
 * 
 */
package com.gitrnd.qaproducer.qa.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className FileValidateProgress.java
 * @description
 * @author DY.Oh
 * @date 2018. 2. 7. 오전 10:55:35
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QAProgress {

	Integer pIdx; // serial
	Integer uIdx; // user idx
	Integer fIdx; // file idx

	Integer qaState;
	Timestamp start_time;
	Timestamp endTime;

	String originName;
	Integer qaCategory;
	String qaType;

	String fileType;
	String errdirectory;
	String errFileName;
	
	Integer prid; //preset id

	String comment;

}
