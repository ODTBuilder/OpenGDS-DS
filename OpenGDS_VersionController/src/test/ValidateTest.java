package test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.git.gdsbuilder.type.geoserver.layer.GeoLayer;
import com.git.gdsbuilder.validator.layer.LayerValidator;
import com.git.gdsbuilder.validator.layer.LayerValidatorImpl;

public class ValidateTest {

	public static void main(String[] args) throws Throwable {
		//
		// read shpfile
		// UnZipFile unZipFile = new UnZipFile();
		// unZipFile.decompress(new
		// File("C:\\Users\\GIT\\Desktop\\검수데이터\\shp\\shptest.zip"));
		// String upZipFilePath = unZipFile.getFileDirectory();
		// String entryName = unZipFile.getEntryName();
		// SHPFileReader fileReader = new SHPFileReader();
		// Map<String, Object> fileNameMap = unZipFile.getFileNamesMap();
		// DTSHPLayerCollection dtCollection = fileReader.read(upZipFilePath,
		// entryName, fileNameMap);
		//
		// parse geolayercollection
		// GeoLayerCollection geoLayerCollection = new GeoLayerCollection();
		// DTSHPLayerList shpLayerList = dtCollection.getShpLayerList();
		// for (int i = 0; i < shpLayerList.size(); i++) {
		// DTSHPLayer shpLayer = shpLayerList.get(i);
		// String layerId = shpLayer.getLayerName();
		// GeoLayer geoLayer = new GeoLayer(shpLayer.getLayerType(), layerId,
		// layerId,
		// shpLayer.getSimpleFeatureCollection());
		// geoLayerCollection.addValidateLayer(geoLayer);
		// if (layerId.toUpperCase().equals("H0010000_MULTILINESTRING")) {
		// geoLayerCollection.setNeatLine(geoLayer);
		// }
		// }
		// ValidateLayerTypeList types = new ValidateLayerTypeList();
		//
		// roadlayer setting
		// List<String> roadLayerList = new ArrayList<>();
		// roadLayerList.add("A0020000_MULTILINESTRING");
		// List<ValidatorOption> roadOptionList = new ArrayList<>();
		// List<String> typeList = new ArrayList<>();
		// typeList.add("MULTIPOINT");
		// ValidatorOption roadOption = new LayerMiss(typeList);
		// roadOptionList.add(roadOption);
		// ValidateLayerType roadType = new ValidateLayerType("도로중심선",
		// roadLayerList, roadOptionList);
		// types.add(roadType);
		//
		// buildinglayer setting
		// List<String> buldingLayerList = new ArrayList<>();
		// buldingLayerList.add("B0010000_MULTILINESTRING");
		// List<ValidatorOption> bulidingOptionList = new ArrayList<>();
		// ValidatorOption buildingOption = new EntityDuplicated();
		// bulidingOptionList.add(buildingOption);
		// ValidateLayerType buildingType = new ValidateLayerType("건물경계",
		// buldingLayerList, bulidingOptionList);
		// types.add(buildingType);
		//
		// collectionValidate
		// CollectionValidator collectionValidator = new
		// CollectionValidator(geoLayerCollection, null, types, null);
		// System.out.println("");

		File file = new File("C:\\Users\\GIT\\Desktop", "test2.shp");
		SimpleFeatureCollection collection = getShpObject(file);
		GeoLayer geoLayer = new GeoLayer("MultiLineString", "coastLine", "coastLine", collection);
		LayerValidator validator = new LayerValidatorImpl(geoLayer);
		DefaultFeatureCollection errLayer = validator.validateConIntersected();

		System.out.println(errLayer.size());
		
		System.out.println("");
		try {
			writeSHP(errLayer, "D:\\Generalization\\test.shp");
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SchemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static SimpleFeatureCollection getShpObject(File shpFile) {

		Map<String, Object> map = new HashMap<String, Object>();
		ShapefileDataStore dataStore;

		String typeName;
		SimpleFeatureCollection collection = null;

		try {
			map.put("url", shpFile.toURI().toURL());
			dataStore = (ShapefileDataStore) DataStoreFinder.getDataStore(map);

			typeName = dataStore.getTypeNames()[0];
			Charset UTF_8 = Charset.forName("EUC-KR");
			dataStore.setCharset(UTF_8);
			SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
			Filter filter = Filter.INCLUDE;
			collection = source.getFeatures(filter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return collection;
	}

	public static void writeSHP(SimpleFeatureCollection simpleFeatureCollection, String filePath)
			throws IOException, SchemaException, NoSuchAuthorityCodeException, FactoryException {

		FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();

		File file = new File(filePath);
		Map map = Collections.singletonMap("url", file.toURI().toURL());

		ShapefileDataStore myData = (ShapefileDataStore) factory.createNewDataStore(map);

		SimpleFeatureType featureType = simpleFeatureCollection.getSchema();
		myData.createSchema(featureType);

		Transaction transaction = new DefaultTransaction("create");

		String typeName = myData.getTypeNames()[0];
		SimpleFeatureSource featureSource = myData.getFeatureSource(typeName);

		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
			featureStore.setTransaction(transaction);
			try {
				featureStore.addFeatures(simpleFeatureCollection);
				transaction.commit();
			} catch (Exception e) {
				e.printStackTrace();
				transaction.rollback();
			} finally {
				transaction.close();
			}
			System.out.println("Success!");
			System.exit(0);
		} else {
			System.out.println(typeName + " does not support read/write access");
			System.exit(1);
		}
	}
}
