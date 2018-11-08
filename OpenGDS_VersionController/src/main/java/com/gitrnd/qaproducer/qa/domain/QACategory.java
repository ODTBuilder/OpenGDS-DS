/**
 * 
 */
package com.gitrnd.qaproducer.qa.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @className QACategory.java
 * @description
 * @author DY.Oh
 * @date 2018. 3. 9. 오전 10:50:02
 */

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class QACategory {

	int cidx;
	String title;
	String support;
}
