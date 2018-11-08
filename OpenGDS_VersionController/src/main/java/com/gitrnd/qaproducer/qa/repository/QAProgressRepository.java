package com.gitrnd.qaproducer.qa.repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gitrnd.qaproducer.qa.domain.QAProgress;
import com.gitrnd.qaproducer.qa.mapper.QAProgressMapper;


@Repository
public class QAProgressRepository {

	@Autowired
	private QAProgressMapper mapper;

	public Integer insertQARequest(QAProgress progress) {
		return mapper.insertQARequest(progress);
	}
}
