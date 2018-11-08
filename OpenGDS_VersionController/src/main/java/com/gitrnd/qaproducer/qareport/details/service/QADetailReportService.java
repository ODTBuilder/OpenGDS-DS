package com.gitrnd.qaproducer.qareport.details.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gitrnd.qaproducer.qareport.details.domain.QADetailReportList;
import com.gitrnd.qaproducer.qareport.details.repository.QADetailReportRepository;

@Service("detatilReportService")
@Transactional
public class QADetailReportService {

	@Autowired
	private QADetailReportRepository detailRepository;

	public QADetailReportList retrieveQADetailReportByPId(JSONObject param) {

		Integer rIdx = (Integer) param.get("rIdx");
		return detailRepository.retrieveQADetailReportByPId(rIdx);
	}
}
