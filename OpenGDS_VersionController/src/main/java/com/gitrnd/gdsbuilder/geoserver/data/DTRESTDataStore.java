package com.gitrnd.gdsbuilder.geoserver.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jdom.Element;

import it.geosolutions.geoserver.rest.decoder.RESTDataStore;

public class DTRESTDataStore extends RESTDataStore {

	public enum DBType {

		POSTGIS("postgis"), ORACLE("oracle"), SHP("shp"), GEOGIG("geogig"), UNKNOWN(null);
		private final String restName;

		private DBType(String restName) {
			this.restName = restName;
		}

		public static DBType get(String restName) {
			for (DBType type : values()) {
				if (type == UNKNOWN) {
					continue;
				}
				if (type.restName.equals(restName)) {
					return type;
				}
			}
			return UNKNOWN;
		}
	};

	public DTRESTDataStore(Element dsElem) {
		super(dsElem);
		// TODO Auto-generated constructor stub
	}

	protected String setConnectionParameter(String paramName, Object value) {

		Map<String, String> oldParams = super.getConnectionParameters();
		Map<String, String> newParams = new HashMap<>();

		Iterator iter = oldParams.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (key.equalsIgnoreCase(paramName)) {
				newParams.put(paramName, value.toString());
			} else {
				newParams.put(key, oldParams.get(key));
			}
		}
		return null;
	}
}
