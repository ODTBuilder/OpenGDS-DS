package com.gitrnd.qaproducer.preset.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gitrnd.qaproducer.common.security.LoginUser;
import com.gitrnd.qaproducer.preset.domain.Preset;
import com.gitrnd.qaproducer.preset.service.PresetService;

@Controller
@RequestMapping("/option")
public class PresetController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PresetController.class);

	@Autowired
	PresetService presetService;

	@RequestMapping(value = "/createpreset.ajax", method = RequestMethod.POST)
	public void createPreset(HttpServletRequest request, HttpServletResponse response,
			@AuthenticationPrincipal LoginUser loginUser) throws Exception {
		LOGGER.info("post: /createpreset.do");

		String qaCat = request.getParameter("category");
		String qaVer = request.getParameter("version");
		int cat = 0;
		switch (qaCat) {
		case "numetrical":
			if (qaVer.equals("qa1")) {
				cat = 1;
			} else if (qaVer.equals("qa2")) {
				cat = 2;
			}
			break;
		case "underground":
			if (qaVer.equals("qa1")) {
				cat = 3;
			} else if (qaVer.equals("qa2")) {
				cat = 4;
			}
			break;
		case "forest":
			cat = 5;
			break;
		default:
			break;
		}
		String name = request.getParameter("name");
		String layerDef = request.getParameter("layer");
		String optionDef = request.getParameter("option");

		Preset preset = new Preset();
		preset.setUidx(loginUser.getIdx());
		preset.setCat(cat);
		preset.setName(name);
		preset.setLayerDef(layerDef);
		preset.setOptionDef(optionDef);
		preset.setBflag(false);// 기본옵션아니기 때문에 디폴트로 false선언

		presetService.createPreset(preset);
	}

	@RequestMapping(value = "/deletePresets.ajax", method = RequestMethod.POST)
	@ResponseBody
	public boolean deletePresets(HttpServletRequest request, @RequestParam(value = "pids", required = true) int[] pids,
			@AuthenticationPrincipal LoginUser loginUser) throws Exception {
		boolean flag = false;

		if (loginUser != null) {

			ArrayList<Preset> prList = new ArrayList<Preset>();

			// Preset VO에 인자값으로 전달받은 pid와 user의 id를 넣어 배열값에 저장
			for (int pid : pids) {
				Preset pr = new Preset();
				pr.setPid(pid);
				pr.setUidx(loginUser.getIdx());

				prList.add(pr);
			}

			flag = presetService.deletePresets(prList);
		}
		return flag;
	}

	@RequestMapping(value = "/updatePreset.ajax", method = RequestMethod.POST)
	@ResponseBody
	public boolean updatePreset(HttpServletRequest request, HttpServletResponse response,
			@AuthenticationPrincipal LoginUser loginUser) throws Exception {
		boolean flag = false;

		if (loginUser != null) {

			String qaCat = request.getParameter("category");
			String qaVer = request.getParameter("version");
			int cat = 0;
			switch (qaCat) {
			case "numetrical":
				if (qaVer.equals("qa1")) {
					cat = 1;
				} else if (qaVer.equals("qa2")) {
					cat = 2;
				}
				break;
			case "underground":
				if (qaVer.equals("qa1")) {
					cat = 3;
				} else if (qaVer.equals("qa2")) {
					cat = 4;
				}
				break;
			case "forest":
				cat = 5;
				break;
			default:
				break;
			}

			int pid = Integer.parseInt(request.getParameter("pid"));
			String name = request.getParameter("name");
			String layerDef = request.getParameter("layer");
			String optionDef = request.getParameter("option");

			Preset preset = new Preset();
			preset.setPid(pid);
			preset.setUidx(loginUser.getIdx());
			preset.setCat(cat);
			preset.setName(name);
			preset.setLayerDef(layerDef);
			preset.setOptionDef(optionDef);
			preset.setBflag(false);// 기본옵션아니기 때문에 디폴트로 false선언

			flag = presetService.updatePreset(preset);
		}
		return flag;
	}

	@RequestMapping(value = "/retrievePresetByUidx.ajax", method = RequestMethod.GET)
	@ResponseBody
	public List<Preset> retrievePresetByUidx(HttpServletRequest request, @AuthenticationPrincipal LoginUser loginUser)
			throws Exception {

		List<Preset> presets;

		if (loginUser != null) {
			int uidx = loginUser.getIdx();
			presets = presetService.retrievePresetNamesByUidx(uidx);
		} else {
			presets = null;
		}
		return presets;
	}
}
