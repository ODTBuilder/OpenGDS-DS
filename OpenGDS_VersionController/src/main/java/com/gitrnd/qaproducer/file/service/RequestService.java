package com.gitrnd.qaproducer.file.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gitrnd.qaproducer.common.worker.Producer;
import com.gitrnd.qaproducer.filestatus.domain.FileStatus;
import com.gitrnd.qaproducer.filestatus.service.FileStatusService;
import com.gitrnd.qaproducer.preset.domain.Preset;
import com.gitrnd.qaproducer.qa.domain.QACategory;
import com.gitrnd.qaproducer.qa.domain.QAProgress;
import com.gitrnd.qaproducer.qa.service.QACategoryService;
import com.gitrnd.qaproducer.qa.service.QAProgressService;
import com.gitrnd.qaproducer.user.domain.User;
import com.gitrnd.qaproducer.user.service.UserService;

@Service
public class RequestService {

	// qa progress
	protected static int fileUpload = 1;

	@Autowired
	private Producer producer;

	@Autowired
	private FileStatusService fileStatusService;

	@Autowired
	private UserService userService;

	@Autowired
	private QAProgressService qapgService;

	@Autowired
	private QACategoryService qaCatService;

	public void requestQAList(List<FileStatus> files, int pidx) {
		for (int k = 0; k < files.size(); k++) {
			JSONObject json = new JSONObject();
			json.put("file", files.get(k).getFid());
			json.put("option", pidx);
			producer.produceMsg(json.toString());
		}
	}

	public void requestQAList(List<FileStatus> files, int cid, String fileformat, String crs, String qaVer,
			String qaType, String bPrid, int prid) {
		for (int k = 0; k < files.size(); k++) {
			// file parsing
			Long fileIdx = (long) files.get(k).getFid();
			int fIdx = fileIdx.intValue();
			FileStatus fileStatus = fileStatusService.retrieveFileStatusById(fIdx);
			String fileName = fileStatus.getFname();

			int uIdx = fileStatus.getUidx();
			User user = userService.retrieveUserByIdx(uIdx);
			String uId = user.getUid();
			String uIdxStr = String.valueOf(uIdx);

			Long catetoryIdx = (long) cid;
			int cIdx = catetoryIdx.intValue();
			QACategory qaCat = qaCatService.retrieveQACategoryByIdx(cIdx);
			String qaTitle = qaCat.getTitle();
			String support = qaCat.getSupport();
			String[] types = support.split(";");

			// insert file upload progress
			QAProgress progress = new QAProgress();
			progress.setQaState(fileUpload);
			progress.setOriginName(fileName);
			progress.setFIdx(fIdx);
			progress.setUIdx(uIdx);
			progress.setQaType(qaTitle);
			progress.setFileType(fileformat);
			progress.setPrid(prid);
			qapgService.insertQARequest(progress);

			int pid = progress.getPIdx();
			JSONObject json = new JSONObject();
			json.put("file", files.get(k).getFid());
			json.put("category", cid);
			json.put("qaVer", qaVer);
			json.put("qaType", qaType);
			json.put("pid", pid);
			json.put("prid", bPrid);
			json.put("fileformat", fileformat);
			json.put("crs", "EPSG:" + crs);

			producer.produceMsg(json.toString());
		}
	}

	public void requestQA(String uid, String file, Preset preset) {
		JSONObject json = new JSONObject();
		// JSONObject layerDef = new JSONObject(preset.getLayerDef());
		JSONObject optionDef = new JSONObject(preset.getOptionDef());
		json.put("uid", uid);
		json.put("path", file);
		// json.put("layerDef", layerDef);
		json.put("optionDef", optionDef);
		producer.produceMsg(json.toString());
	}
}
