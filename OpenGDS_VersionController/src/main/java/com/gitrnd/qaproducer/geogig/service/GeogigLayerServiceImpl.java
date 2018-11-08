/**
 * 
 */
package com.gitrnd.qaproducer.geogig.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Service;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;
import com.gitrnd.gdsbuilder.geogig.command.repository.DiffRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.LogRepository;
import com.gitrnd.gdsbuilder.geogig.type.GeogigDiff;
import com.gitrnd.gdsbuilder.geogig.type.GeogigDiff.Diff;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRepositoryLog;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;

/**
 * @author GIT
 *
 */
@Service("layerService")
public class GeogigLayerServiceImpl implements GeogigLayerService {

//	@Override
//	public GeogigRepositoryLog logLayer(DTGeoserverManager geoserverManager, String repoName, String layerName,
//			String limit, String until, String head) throws JAXBException {
//
//		String url = geoserverManager.getRestURL();
//		String user = geoserverManager.getUsername();
//		String pw = geoserverManager.getPassword();
//
//		LogRepository logRepos = new LogRepository();
//		GeogigRepositoryLog log = null;
//		try {
//			log = logRepos.executeCommand(url, user, pw, repoName, layerName, limit, until);
//		} catch (GeogigCommandException e) {
//			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigRepositoryLog.class);
//			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//			log = (GeogigRepositoryLog) unmarshaller.unmarshal(new StringReader(e.getMessage()));
//		}
//		return log;
//	}

	@Override
	public GeogigDiff diffLayer(DTGeoserverManager geoserverManager, String repoName, int oldIndex, int newIndex,
			String layerName) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		String oldTreeish = "HEAD";
		String newTreeish = "HEAD";
		if (newIndex > 0) {
			newTreeish += "~" + newIndex;
		}
		oldTreeish += "~" + oldIndex;

		DiffRepository diffRepos = new DiffRepository();
		GeogigDiff diff = null;
		try {
			diff = diffRepos.executeCommand(url, user, pw, repoName, oldTreeish, newTreeish, layerName, null);
			String nextPage = diff.getNextPage();
			if (nextPage != null) {
				Integer page = 1;
				List<Diff> diffs = new ArrayList<>();
				diffs.addAll(diff.getDiffs());
				while (nextPage != null) {
					GeogigDiff nextDiff = diffRepos.executeCommand(url, user, pw, repoName, oldTreeish, newTreeish,
							layerName, page);
					diffs.addAll(nextDiff.getDiffs());
					nextPage = nextDiff.getNextPage();
				}
				diff.setDiffs(diffs);
			}
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigDiff.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			diff = (GeogigDiff) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return diff;
	}
}
