package com.gitrnd.qaproducer.preset.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gitrnd.qaproducer.filestatus.domain.FileStatus;
import com.gitrnd.qaproducer.preset.domain.Preset;

@Mapper
public interface PresetMapper {

	public Preset retrieveBasePreset(int cat);
	
	public Preset retrievePridByBasePreset(int cat);

	public Preset retrievePresetById(int pid);
	
	public Preset retrieveCatByPreset(int pid);
	
	public int updatePreset(Preset preset);

	public int deletePresets(ArrayList<Preset> prList);

	public List<Preset> retrievePresetByUidx(int uidx);

	public List<Preset> retrievePresetNamesByUidx(int uidx);

	public void createPreset(Preset preset);
	
	public Preset retrievePresetByIdAndUidx(Preset preset);
}
