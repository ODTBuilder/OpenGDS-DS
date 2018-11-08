package com.gitrnd.qaproducer.qareport.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gitrnd.qaproducer.qareport.domain.QAReport;
import com.gitrnd.qaproducer.qareport.repository.QAReportRepository;

@Service("reportService")
@Transactional
public class QAReportService {

	@Autowired
	private QAReportRepository reportService;

	@Transactional(readOnly = true)
	public QAReport retrieveQAReportByPId(JSONObject param) {

		Integer pIdx = (Integer) param.get("pIdx");
		return reportService.retrieveQAReportByPId(pIdx);
	}
}
