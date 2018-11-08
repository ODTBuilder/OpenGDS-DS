package com.gitrnd.qaproducer.qareport.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gitrnd.qaproducer.qareport.domain.QAReport;
import com.gitrnd.qaproducer.qareport.mapper.QAReportMapper;

@Repository
public class QAReportRepository {

	@Autowired
	private QAReportMapper mapper;

	/**
	 * @param rIdx
	 * @return
	 */
	public QAReport retrieveQAReportByPId(int pIdx) {
		return mapper.retrieveQAReportByPId(pIdx);
	}
}
