/**
 * 
 */
package com.gitrnd.qaproducer.qa.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gitrnd.qaproducer.qa.service.QAMobileService;

/**
 * @author GIT
 *
 */
@Controller
@RequestMapping("/mobile")
public class QAMobileController {

	@Autowired
	@Qualifier("mobileService")
	QAMobileService mobileService;

	@RequestMapping(value = "/validate.do", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject validate(HttpServletRequest request, @RequestBody JSONObject param) {
		return mobileService.validate(param);
	}
}
