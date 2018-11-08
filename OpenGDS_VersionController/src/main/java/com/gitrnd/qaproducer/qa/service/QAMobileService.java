/**
 * 
 */
package com.gitrnd.qaproducer.qa.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gitrnd.qaproducer.common.worker.Producer;

/**
 * @author GIT
 *
 */
@Service("mobileService")
public class QAMobileService {

	@Autowired
	private Producer producer;

	/**
	 * @param param
	 * @return
	 */
	public JSONObject validate(JSONObject param) {

		return (JSONObject) producer.produceMobileMsg(param.toString());
	}
}
