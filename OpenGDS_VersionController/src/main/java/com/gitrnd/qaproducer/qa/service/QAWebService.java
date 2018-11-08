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
@Service("webService")
public class QAWebService {

	@Autowired
	private Producer producer;

	/**
	 * @param param
	 * @return
	 */
	public void validate(JSONObject param) {

		producer.produceWebMsg(param.toString());
	}
}
