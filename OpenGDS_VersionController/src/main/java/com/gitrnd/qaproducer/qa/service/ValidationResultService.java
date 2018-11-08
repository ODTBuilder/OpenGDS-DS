package com.gitrnd.qaproducer.qa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gitrnd.qaproducer.filestatus.domain.FileStatus;
import com.gitrnd.qaproducer.filestatus.service.FileStatusService;
import com.gitrnd.qaproducer.qa.domain.ServerSideVO;
import com.gitrnd.qaproducer.qa.domain.ValidationResult;
import com.gitrnd.qaproducer.qa.repository.ValidationResultRepository;

@Service
@Transactional
public class ValidationResultService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationResultService.class);

	@Autowired
	private ValidationResultRepository validationResultRepository;

	@Autowired
	private FileStatusService fileStatusService;

	/**
	 * DataTable Server Side 요청 처리 Service
	 */
	@Transactional(readOnly = true)
	public JSONObject retrieveValidationResultByUidx(HashMap<String, Object> input, ServerSideVO serverSideVO,
			int idx) {
		// 반환할 데이터들
		JSONObject dataTable = new JSONObject();
		JSONArray data = null;

		int draw = serverSideVO.getDrawCount();
		
		// DataTables의 테이블이 처음 load 되었을때 페이지의 위치
		int start = serverSideVO.getStartIndex();
		
		// 화면에 보여질 테이블의 행 개수
		int length = serverSideVO.getDisplayLength();
		
		// 정렬을 적용하려는 column의 번호. 첫번째 column은 1
		int order_idx = serverSideVO.getOrderColumn();
		
		// 오름차순 또는 내림차순
		String order_direct = serverSideVO.getOrderDirection();

		// 전체 행의 개수
		int count = validationResultRepository.countValidationResultByUidx(idx);
		
		// DB로부터 전달받은 데이터를 DataTables parmeter 형식에 맞게 파싱
		data = parseServerData(validationResultRepository.retrieveValidationResultByUidx(draw, start, length, order_idx,
				order_direct, idx));

		dataTable.put("draw", draw);
		dataTable.put("recordsTotal", count);
		dataTable.put("recordsFiltered", count);
		dataTable.put("data", data);

		return dataTable;
	}

	/**
	 * DB로부터 전달받은 JSONArray 데이터를 DataTable 형식에 맞게 파싱하는 함수
	 * 
	 * @param serverDataList
	 * @return
	 * @Author hochul
	 * @Date 2018. 8. 20.
	 */
	private JSONArray parseServerData(JSONArray serverDataList) {
		
		// JSONArray 데이터를 추출하기 위해 DB로부터 전달받은 JSONArray 데이터를 JSONString으로 변환
		String json = serverDataList.toJSONString();
		
		
		JSONParser parser = new JSONParser();
		
		// 추출된 데이터
		JSONArray list = null;
		
		/*
		 * DataTables "data" parameter 구조에 맞는 JSONArray를 생성
		 * DataTables 공식 홈페이지 Manual의 serverside-processing 부분 참조
		 * https://datatables.net/manual/server-side
		 */
		JSONArray result = new JSONArray();
		
		try {
			list = (JSONArray)parser.parse(json);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < list.size(); i++){
			// row data 추출
			JSONObject data = (JSONObject)list.get(i);
			
			// tr tag 부분에 적용될 값들을 담은 JSONObject
			JSONObject param = new JSONObject();
			
			// td tag 부분의 값들을 담은 JSONObject
			JSONObject rowData = new JSONObject();
			
			Long no = (Long)data.get("no");
			Long pidx = (Long)data.get("pidx");
			Long fidx = (Long)data.get("fidx");
			Long uidx = (Long)data.get("uidx");
			String zipName = (String)data.get("zipName");
			String createTime = (String)data.get("createTime");
			String endTime = (String)data.get("endTime");
			String qaType = (String)data.get("qaType");
			String format = (String)data.get("format");
			Long stateCode = (Long)data.get("stateCode");
			String state = (String)data.get("state");
			String errFileName = (String)data.get("errFileName");
			
			String download = (String)data.get("download");
			String comment = (String)data.get("comment");
			
			rowData.put("value", pidx);
			rowData.put("fid", fidx);
			rowData.put("state", stateCode);
			rowData.put("filename", errFileName);
			
			param.put("DT_RowData", rowData);
			param.put("no", no);
			param.put("zipName", zipName);
			param.put("createTime", createTime);
			param.put("endTime", endTime);
			param.put("qaType", qaType);
			param.put("format", format);
			param.put("state", state);
			param.put("download", download);
			param.put("comment", comment);
			
			result.add(param);
		}
		return result;
	}

	/**
	 * tb_file 테이블과 tb_progress테이블이 조인 된 결과값에 대하여 인자값 pid에 일치하고 tb_file.fidx와
	 * tb_progress.fid가 일치하는 행을 반환한다.
	 * 
	 * @param idx
	 * @return com.gitrnd.qaproducer.domain.ValidationResult
	 * @Author hochul
	 * @Date 2018. 4. 10.
	 */
	@Transactional(readOnly = true)
	public ValidationResult retrieveValidationResultByPidx(int idx) {
		return validationResultRepository.retrieveValidationResultByPidx(idx);
	}

	/**
	 * 검수 작업 내용을 DB 테이블에서 삭제한다. 작업 내용을 삭제하기전 검수 원본 파일 이력 테이블에서 fid에 적합한 행을 삭제한 후
	 * 작업 내용을 삭제한다. 삭제할 작업 내용은 배열 형식으로 작업 내용 p_idx 데이터를 받아 삭제한다.
	 * 
	 * @param userId
	 * @param list
	 * @return
	 * @Author hochul
	 * @Date 2018. 4. 10.
	 */
	@Transactional
	public boolean deleteValidationResult(ArrayList<ValidationResult> vrList) {
		boolean flag = false;

		ArrayList<FileStatus> fsList = new ArrayList<FileStatus>();

		for (ValidationResult vr : vrList) {
			// fid값 검색
			ValidationResult result = validationResultRepository.retrieveValidationResultByPidx(vr.getPidx());

			FileStatus fs = new FileStatus();
			fs.setFid(result.getFidx());
			fs.setUidx(vr.getUidx());

			// fsList 배열에 추가
			fsList.add(fs);
		}

		try {
			// 검수 작업 내용 삭제
			flag = validationResultRepository.deleteValidationResult(vrList);
		} catch (RuntimeException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

		for (FileStatus temp : fsList) {
			// 검수 원본 파일 이력 테이블 삭제
			if (fileStatusService.deleteFileStatus(temp)) {
				LOGGER.info("fid({}) table delete success!", temp.getFid());
			} else {
				LOGGER.info("ERROR!: fid({}) table delete fail!", temp.getFid());
			}
		}

		return flag;
	}
}
