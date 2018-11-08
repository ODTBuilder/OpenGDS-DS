package com.gitrnd.qaproducer.qareport.details.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gitrnd.qaproducer.qareport.details.domain.QADetailReportList;

@Mapper
public interface QADetailReportMapper {

	/**
	 * @param rIdx
	 * @return
	 */
	public QADetailReportList retrieveQADetailReportByPId(int rIdx);

}
