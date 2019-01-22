package com.gitrnd.gdsbuilder.geoserver.data;

import java.util.Map;

import it.geosolutions.geoserver.rest.decoder.RESTDataStore;

public class DTGSGeogigDatastoreEncoder extends DTGSAbstractDatastoreEncoder {

	static final String TYPE = "GeoGIG";

//	static final String DEFALUT_GEOGIG_REPOSITORY = "repository";
//	static final String DEFALUT_BRANCH = "branch";
//	static final String DEFALUT_NAMESPACE = "namespace";
//	static final String DEFALUT_AUTOINDEXING = "false";
//
//	public DTGSGeogigDatastoreEncoder(String storeName) {
//		super(storeName);
//		setRepository(DEFALUT_GEOGIG_REPOSITORY);
//		setAutoIndexing(DEFALUT_AUTOINDEXING);
//		setNamespace(DEFALUT_NAMESPACE);
//		setBranch(DEFALUT_BRANCH);
//	}

	public DTGSGeogigDatastoreEncoder(RESTDataStore store) {
		super(store);
		setName(store.getName());
		setType(store.getStoreType());
		setEnabled(store.isEnabled());

		Map<String, String> params = store.getConnectionParameters();
		for (String key : params.keySet()) {
			connectionParameters.set(key, params.get(key));
		}
	}

	@Override
	protected String getValidType() {
		return TYPE;
	}

	public void setRepository(String repository) {
		connectionParameters.set("geogig_repository", repository);
	}

	public void setAutoIndexing(String defalutAutoindexing) {
		connectionParameters.set("autoIndexing", defalutAutoindexing);
	}

	public void setNamespace(String namespace) {
		connectionParameters.set("namespace", namespace);
	}

	public void setBranch(String branch) {
		connectionParameters.set("branch", branch);
	}

}
