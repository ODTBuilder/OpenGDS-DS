package com.gitrnd.gdsbuilder.geoserver.data;

import java.util.Map;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.encoder.GSAbstractStoreEncoder;
import it.geosolutions.geoserver.rest.encoder.utils.NestedElementEncoder;

/**
 * Geogig Repository 기반 Geoserver Abstract Datastore Encoder.
 * 
 * @author DY.Oh
 *
 */
public abstract class DTGSAbstractDatastoreEncoder extends GSAbstractStoreEncoder {

	final static String ROOT = "dataStore";

	NestedElementEncoder connectionParameters = new NestedElementEncoder("connectionParameters");

	/**
	 * Geoserver Datastore Encoder 생성.
	 * 
	 * @param storeName geoserver ds명
	 */
	public DTGSAbstractDatastoreEncoder(String storeName) {
		super(GeoServerRESTPublisher.StoreType.DATASTORES, ROOT);
		// Add mandatory parameter
		ensureValidName(storeName);
		setName(storeName);

		// Add connection parameters
		addContent(connectionParameters.getRoot());
	}

	/**
	 * Create a datastore encoder from a store read from server.
	 * 
	 * @param store The existing store.
	 * @throws IllegalArgumentException if store type or mandatory parameters are
	 *                                  not valid
	 */
	public DTGSAbstractDatastoreEncoder(RESTDataStore store) {
		this(store.getName());

		// Match datastore type
		ensureValidType(store.getStoreType());

		// Copy store parameters
		setDescription(store.getDescription());
		setEnabled(store.isEnabled());

		// Copy connection parameters - bulk
		Map<String, String> params = store.getConnectionParameters();
		for (String key : params.keySet()) {
			connectionParameters.set(key, params.get(key));
		}
	}

	/**
	 * The type of the implementing datastore.
	 */
	protected abstract String getValidType();

}
