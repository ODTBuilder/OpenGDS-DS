/**
 * 
 */
package com.gitrnd.qaproducer.qareport.domain;

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
public class QAReport {

	Integer r_idx;
	String layerId;
	Integer layerCount;
	Integer featureCount;
	Integer normalCount;
	Integer errCount;
	Integer exceptCount;
	String comment;
	Integer pIdx;

}
