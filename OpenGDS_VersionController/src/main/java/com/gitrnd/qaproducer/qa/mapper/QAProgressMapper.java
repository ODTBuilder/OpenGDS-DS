package com.gitrnd.qaproducer.qa.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gitrnd.qaproducer.qa.domain.QAProgress;


@Mapper
public interface QAProgressMapper {

	public Integer insertQARequest(QAProgress progress);


}
