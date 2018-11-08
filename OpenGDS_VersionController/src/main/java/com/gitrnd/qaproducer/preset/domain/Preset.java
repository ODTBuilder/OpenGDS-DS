package com.gitrnd.qaproducer.preset.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Preset {

	private int pid;
	private String name;
	private int cat;
	private String title;
	private String layerDef;
	private String optionDef;
	private int uidx;
	private boolean bflag;
	private String support;

}
