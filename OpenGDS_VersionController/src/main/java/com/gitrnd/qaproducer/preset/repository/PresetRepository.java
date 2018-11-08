package com.gitrnd.qaproducer.preset.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gitrnd.qaproducer.filestatus.domain.FileStatus;
import com.gitrnd.qaproducer.preset.domain.Preset;
import com.gitrnd.qaproducer.preset.mapper.PresetMapper;

@Repository
public class PresetRepository {

	@Autowired
	private PresetMapper presetMapper;

	public Preset retrieveBasePreset(int cat) {
		return presetMapper.retrieveBasePreset(cat);
	}
	
	public Preset retrievePridByBasePreset(int cat) {
		return presetMapper.retrievePridByBasePreset(cat);
	}

	public Preset retrievePresetById(int pid) {
		return presetMapper.retrievePresetById(pid);
	}

	public Preset retrieveCatByPreset(int pid) {
		return presetMapper.retrieveCatByPreset(pid);
	}
	
	
	public List<Preset> retrievePresetByUidx(int uidx) {
		return presetMapper.retrievePresetByUidx(uidx);
	}

	public List<Preset> retrievePresetNamesByUidx(int uidx) {
		return presetMapper.retrievePresetNamesByUidx(uidx);
	}

	public void createPreset(Preset preset) {
		presetMapper.createPreset(preset);
	}

	public boolean updatePreset(Preset preset) {
		boolean flag = false;
		int num = presetMapper.updatePreset(preset);
		if (num > 0) {
			flag = true;
		}
		return flag;
	}

	public boolean deletePresets(ArrayList<Preset> prList) {
		boolean flag = false;
		int num = presetMapper.deletePresets(prList);
		if (num > 0) {
			flag = true;
		}
		return flag;
	}

	public Preset retrievePresetByIdAndUidx(Preset preset) {
		return presetMapper.retrievePresetByIdAndUidx(preset);
	}

}
