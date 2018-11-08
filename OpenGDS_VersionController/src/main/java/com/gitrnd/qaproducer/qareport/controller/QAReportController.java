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

import com.gitrnd.qaproducer.qareport.domain.QAReport;
import com.gitrnd.qaproducer.qareport.service.QAReportService;

/**
 * @author GIT
 *
 */
@Controller
@RequestMapping(value = "/qaReport")
public class QAReportController {

	@Autowired
	@Qualifier("reportService")
	QAReportService reportService;

	@RequestMapping(value = "/report.ajax", method = RequestMethod.POST)
	public QAReport retrieveQAReportByPId(JSONObject param) {
		return reportService.retrieveQAReportByPId(param);
	}
}
