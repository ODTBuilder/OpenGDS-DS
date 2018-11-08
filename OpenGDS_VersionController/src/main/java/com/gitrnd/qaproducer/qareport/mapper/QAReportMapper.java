package com.gitrnd.qaproducer.qareport.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gitrnd.qaproducer.qareport.domain.QAReport;

@Mapper
public interface QAReportMapper {

	/**
	 * @param rIdx
	 * @return
	 */
	public QAReport retrieveQAReportByPId(int pIdx);

}
