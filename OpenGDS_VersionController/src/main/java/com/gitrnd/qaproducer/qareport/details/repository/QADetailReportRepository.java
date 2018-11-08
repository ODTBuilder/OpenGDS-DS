package com.gitrnd.qaproducer.qareport.details.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gitrnd.qaproducer.qareport.details.domain.QADetailReportList;
import com.gitrnd.qaproducer.qareport.details.mapper.QADetailReportMapper;

@Repository
public class QADetailReportRepository {

	@Autowired
	private QADetailReportMapper mapper;

	/**
	 * @param rIdx
	 * @return
	 */
	public QADetailReportList retrieveQADetailReportByPId(int rIdx) {
		return mapper.retrieveQADetailReportByPId(rIdx);
	}
}
