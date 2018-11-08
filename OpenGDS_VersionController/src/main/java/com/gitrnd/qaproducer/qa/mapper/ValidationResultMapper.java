package com.gitrnd.qaproducer.qa.mapper;

import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.json.simple.JSONArray;

import com.gitrnd.qaproducer.qa.domain.ValidationResult;

@Mapper
public interface ValidationResultMapper {

	int countValidationResultByUidx(int idx);
	
	JSONArray retrieveValidationResultByUidx(Map<String,Object> params);
	
	ValidationResult retrieveValidationResultByPidx(int idx);
	
	int deleteValidationResult(ArrayList<ValidationResult> vrList);
}
