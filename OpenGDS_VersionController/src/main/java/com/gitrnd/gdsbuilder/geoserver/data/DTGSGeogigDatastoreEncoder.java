package com.gitrnd.gdsbuilder.geoserver.data;

import java.util.Map;

import it.geosolutions.geoserver.rest.decoder.RESTDataStore;

/**
 * Geogig Repository 기반 Geoserver Datastore Encoder.
 * 
 * @author DY.Oh
 *
 */
public class DTGSGeogigDatastoreEncoder extends DTGSAbstractDatastoreEncoder {

	/**
	 * Geoserver Datastore 타입
	 */
	static final String TYPE = "GeoGIG";

	/**
	 * Geogig Repository 기반 Geoserver Datastore Encoder 생성.
	 * 
	 * @param store Geogig Datastore 생성 요청 XML REST Object
	 */
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
