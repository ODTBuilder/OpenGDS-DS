/**
 * 
 */
package com.gitrnd.qaproducer.qareport.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gitrnd.qaproducer.qareport.details.domain.QADetailReportList;
import com.gitrnd.qaproducer.qareport.details.service.QADetailReportService;

/**
 * @author GIT
 *
 */
@Controller
@RequestMapping(value = "/qaReport")
public class QADetailReportController {

	@Autowired
	@Qualifier("detatilReportService")
	QADetailReportService detailService;

	@RequestMapping(value = "/detailReport.ajax", method = RequestMethod.POST)
	public QADetailReportList retrieveQADetailReportByPId(JSONObject param) {
		return detailService.retrieveQADetailReportByPId(param);
	}
}
