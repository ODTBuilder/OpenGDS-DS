package com.gitrnd.qaproducer.preset.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gitrnd.qaproducer.preset.domain.Preset;
import com.gitrnd.qaproducer.preset.repository.PresetRepository;

@Service
@Transactional
public class PresetService {

	@Autowired
	private PresetRepository presetRepository;

	@Transactional(readOnly = true)
	public Preset retrieveBasePreset(int cat) {
		return presetRepository.retrieveBasePreset(cat);
	}

	@Transactional(readOnly = true)
	public Preset retrievePridByBasePreset(int cat) {
		return presetRepository.retrievePridByBasePreset(cat);
	}

	@Transactional(readOnly = true)
	public Preset retrievePresetById(int pid) {
		return presetRepository.retrievePresetById(pid);
	}

	@Transactional(readOnly = true)
	public Preset retrieveCatByPreset(int pid) {
		return presetRepository.retrieveCatByPreset(pid);
	}

	@Transactional(readOnly = true)
	public List<Preset> retrievePresetByUidx(int uidx) {
		return presetRepository.retrievePresetByUidx(uidx);
	}

	@Transactional(readOnly = true)
	public List<Preset> retrievePresetNamesByUidx(int uidx) {
		return presetRepository.retrievePresetNamesByUidx(uidx);
	}

	@Transactional
	public void createPreset(Preset preset) {
		presetRepository.createPreset(preset);
	}

	@Transactional
	public boolean updatePreset(Preset preset) {
		return presetRepository.updatePreset(preset);
	}

	@Transactional
	public boolean deletePresets(ArrayList<Preset> prList) {
		return presetRepository.deletePresets(prList);
	}

	@Transactional
	public Preset retrievePresetByIdAndUidx(Preset preset) {
		return presetRepository.retrievePresetByIdAndUidx(preset);
	}

}
