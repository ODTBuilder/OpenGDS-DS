package com.gitrnd.qaproducer.file.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gitrnd.qaproducer.common.exception.ValidationAuthException;
import com.gitrnd.qaproducer.common.security.LoginUser;
import com.gitrnd.qaproducer.controller.AbstractController;
import com.gitrnd.qaproducer.file.service.DeleteFileService;
import com.gitrnd.qaproducer.file.service.DownloadService;
import com.gitrnd.qaproducer.file.service.RequestService;
import com.gitrnd.qaproducer.file.service.UploadService;
import com.gitrnd.qaproducer.filestatus.domain.FileStatus;
import com.gitrnd.qaproducer.preset.domain.Preset;
import com.gitrnd.qaproducer.preset.service.PresetService;
import com.gitrnd.qaproducer.qa.domain.ValidationResult;
import com.gitrnd.qaproducer.qa.service.ValidationResultService;

@Controller
public class FileController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

	@Autowired
	PresetService presetService;

	@Autowired
	RequestService requestService;

	@Autowired
	UploadService uploadService;

	@Autowired
	DownloadService downloadService;

	@Autowired
	DeleteFileService deleteFileService;

	@Autowired
	ValidationResultService validationResultService;
	
	@RequestMapping(value = "/deleteList.ajax", method = RequestMethod.POST)
	@ResponseBody
	public boolean deleteList(HttpServletRequest request, @RequestParam(value = "list", required = true) int[] list,
			@RequestParam(value = "file", required = true) String[] filenames,
			@AuthenticationPrincipal LoginUser loginUser) throws Exception {
		boolean flag = false;

		// 로컬 경로에 저장되어 있는 에러 레이어 zip파일을 삭제
		if (deleteFileService.deleteErrorZipFile(loginUser, filenames)) {
			// zip파일 삭제 성공시 수행

			LOGGER.info("{} file delete success!", filenames.toString());

			ArrayList<ValidationResult> vrList = new ArrayList<ValidationResult>();

			for (int pid : list) {
				ValidationResult vr = new ValidationResult();
				vr.setPidx(pid);
				vr.setUidx(loginUser.getIdx());
				vrList.add(vr);
			}

			// list 배열에 있는 pk 값을 가진 모든 작업내용을 tb_progress 테이블에서 삭제
			flag = validationResultService.deleteValidationResult(vrList);
		} else {
			LOGGER.info("ERROR!: {} file 삭제 실패", filenames.toString());
		}
		return flag;
	}

	@RequestMapping(value = "/downloadzip.do", method = RequestMethod.GET)
	public void downloadProcess(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("time") String time, @RequestParam("file") String file, @RequestParam("user") String user)
			throws IOException {
		downloadService.downloadZip(response, time, file, user);
	}

	@RequestMapping(value = "/downloaderror.do", method = RequestMethod.GET)
	public void downloadErrorProcess(HttpServletResponse response, @RequestParam("time") String time,
			@RequestParam("file") String file, @AuthenticationPrincipal LoginUser loginUser) throws IOException {
		downloadService.downloadError(response, time, file, loginUser);
	}

	@RequestMapping(value = "/uploaderror.do", method = RequestMethod.POST)
	public void uploadErrorProcess(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		uploadService.SaveErrorFile(request);
		if (deleteFileService.deleteOriginalZipFile(request.getParameter("user"),
				Integer.parseInt(request.getParameter("fid")))) {
			LOGGER.info("fid: {} file delete success!", request.getParameter("fid"));
		} else {
			LOGGER.info("ERROR!: fid: {} file delete fail!", request.getParameter("fid"));
		}
	}

	@RequestMapping(value = "/upload.do", method = RequestMethod.POST)
	public void uploadProcess(MultipartHttpServletRequest request, HttpServletResponse response,
			@AuthenticationPrincipal LoginUser loginUser) throws Exception {
		LOGGER.info("access: /uploadProcess.do");

		String qaVer = request.getParameter("qaver");
		String qaType = request.getParameter("qatype");
		String prid = request.getParameter("pid");
		String crs = request.getParameter("crs");
		String fileformat = request.getParameter("fileformat");
		Preset prst = null;

		if (prid.equals("nonset")) {
			if (qaVer.equals("qa1")) {
				switch (qaType) {
				case "nm1":
					break;
				case "nm5":
					prst = presetService.retrievePridByBasePreset(1);
					break;
				case "nm25":
					break;
				case "ug1":
					break;
				case "ug5":
					prst = presetService.retrievePridByBasePreset(3);
					break;
				case "ug25":
					break;
				case "fr1":
					break;
				case "fr5":
					prst = presetService.retrievePridByBasePreset(5);
					break;
				case "fr25":
					break;
				default:
					break;
				}
			} else if (qaVer.equals("qa2")) {
				switch (qaType) {
				case "nm1":
					break;
				case "nm5":
					prst = presetService.retrievePridByBasePreset(2);
					break;
				case "nm25":
					break;
				case "ug1":
					break;
				case "ug5":
					prst = presetService.retrievePridByBasePreset(4);
					break;
				case "ug25":
					break;
				case "fr1":
					break;
				case "fr5":
					prst = presetService.retrievePridByBasePreset(5);
					break;
				case "fr25":
					break;
				default:
					break;
				}
			}
		} else {
			prst = presetService.retrieveCatByPreset(Integer.parseInt(prid));
		}

		int nowCat = prst.getCat();
		String nowAuthString = "";
		if (nowCat == 1 || nowCat == 2) {
			nowAuthString = "DIGITAL";
		} else if (nowCat == 3 || nowCat == 4) {
			nowAuthString = "UNDERGROUND";
		} else if (nowCat == 5) {
			nowAuthString = "FOREST";
		}
		GrantedAuthority digital = new SimpleGrantedAuthority("DIGITAL");
		GrantedAuthority under = new SimpleGrantedAuthority("UNDERGROUND");
		GrantedAuthority forest = new SimpleGrantedAuthority("FOREST");
		GrantedAuthority allpass = new SimpleGrantedAuthority("ALLPASS");
		GrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");

		boolean isAuthorized = false;

		if (nowAuthString.equals("DIGITAL") && (loginUser.getAuthorities().contains(digital)
				|| loginUser.getAuthorities().contains(allpass) || loginUser.getAuthorities().contains(admin))) {
			isAuthorized = true;
		} else if (nowAuthString.equals("UNDERGROUND") && (loginUser.getAuthorities().contains(under)
				|| loginUser.getAuthorities().contains(allpass) || loginUser.getAuthorities().contains(admin))) {
			isAuthorized = true;
		} else if (nowAuthString.equals("FOREST") && (loginUser.getAuthorities().contains(forest)
				|| loginUser.getAuthorities().contains(allpass) || loginUser.getAuthorities().contains(admin))) {
			isAuthorized = true;
		}

		if (isAuthorized) {
			// 옵션또는 파일이 제대로 넘어오지 않았을때 강제로 예외발생
			if (qaVer == null || qaType == null || prid == null || prst == null) {
				throw new Exception("인자가 부족합니다. 다시 요청해주세요.");
			} else if (fileformat == null) {
				throw new Exception("파일포맷을 설정해주세요.");
			} else {
				List<FileStatus> files = uploadService.SaveFile(request, loginUser);
				requestService.requestQAList(files, prst.getCat(), fileformat, crs, qaVer, qaType, prid, prst.getPid());
			}
		} else {
			throw new ValidationAuthException("해당 검수 요청 권한이 없습니다.");
		}
	}
}
