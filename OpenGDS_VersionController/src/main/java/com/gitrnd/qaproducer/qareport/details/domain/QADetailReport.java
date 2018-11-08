/**
 * 
 */
package com.gitrnd.qaproducer.qareport.details.domain;

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
public class QADetailReport {

	Integer rd_idx;
	String refLayerId;
	String featureId;
	String refFeatureId;
	String errType;
	String errName;
	String errPoint;
	String comment;
	Integer rIdx;

	/**
	 * @param refLayerId
	 * @param featureId
	 * @param reffeatureId
	 * @param errType
	 * @param errName
	 * @param errPoint
	 * @param comment
	 * @param rIdx
	 */
	public QADetailReport(String refLayerId, String featureId, String reffeatureId, String errType, String errName,
			String errPoint, String comment, Integer rIdx) {
		super();
		this.refLayerId = refLayerId;
		this.featureId = featureId;
		this.refFeatureId = reffeatureId;
		this.errType = errType;
		this.errName = errName;
		this.errPoint = errPoint;
		this.comment = comment;
		this.rIdx = rIdx;
	}

}
