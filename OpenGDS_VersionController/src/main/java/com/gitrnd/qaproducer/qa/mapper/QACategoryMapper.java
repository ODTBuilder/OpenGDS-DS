package com.gitrnd.qaproducer.qa.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gitrnd.qaproducer.qa.domain.QACategory;


/**
 * @className QACategoryMapper.java
 * @description
 * @author DY.Oh
 * @date 2018. 3. 9. 오전 10:49:10
 */

@Mapper
public interface QACategoryMapper {

	/**
	 * @author DY.Oh
	 * @Date 2018. 3. 9. 오전 10:51:17
	 * @param idx
	 * @return QACategory
	 * @decription
	 */
	public QACategory retrieveQACategoryByIdx(int idx);

}
