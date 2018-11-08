/**
 * 
 */
package com.gitrnd.qaproducer.geogig.service;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.gitrnd.gdsbuilder.geogig.GeogigCommandException;
import com.gitrnd.gdsbuilder.geogig.command.repository.AddRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.CommitRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.DeleteRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.InitRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.PullRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.PushRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.remote.AddRemoteRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.remote.ListRemoteRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.remote.PingRemoteRepository;
import com.gitrnd.gdsbuilder.geogig.command.repository.remote.RemoveRemoteRepository;
import com.gitrnd.gdsbuilder.geogig.command.transaction.BeginTransaction;
import com.gitrnd.gdsbuilder.geogig.command.transaction.EndTransaction;
import com.gitrnd.gdsbuilder.geogig.type.GeogigAdd;
import com.gitrnd.gdsbuilder.geogig.type.GeogigCommit;
import com.gitrnd.gdsbuilder.geogig.type.GeogigFetch;
import com.gitrnd.gdsbuilder.geogig.type.GeogigPull;
import com.gitrnd.gdsbuilder.geogig.type.GeogigPush;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRemoteRepository;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRepositoryDelete;
import com.gitrnd.gdsbuilder.geogig.type.GeogigRepositoryInit;
import com.gitrnd.gdsbuilder.geogig.type.GeogigTransaction;
import com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager;
import com.gitrnd.qaproducer.common.security.LoginUser;

/**
 * @author GIT
 *
 */
@Service("reposService")
public class GeogigRepositoryServiceImple implements GeogigRepositoryService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gitrnd.qaproducer.geogig.service.GeogigRepositoryService#initRepository(
	 * com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager,
	 * com.gitrnd.qaproducer.common.security.LoginUser, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public GeogigRepositoryInit initRepository(DTGeoserverManager geoserverManager, LoginUser loginUser,
			String repoName, String dbHost, String dbPort, String dbName, String dbSchema, String dbUser,
			String dbPassword, String remoteName, String remoteURL) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		String authorName = loginUser.getUsername();
		String authorEmail = loginUser.getEmail();

		InitRepository initRepos = new InitRepository();
		GeogigRepositoryInit geogigReposInit = null;
		try {
			geogigReposInit = initRepos.executeCommand(url, user, pw, repoName, dbHost, dbPort, dbName, dbSchema,
					dbUser, dbPassword, authorName, authorEmail);
			if (remoteName != null && remoteURL != null) {
				try {
					String initReposName = geogigReposInit.getRepo().getName();
					// add remote
					AddRemoteRepository addRemote = new AddRemoteRepository();
					addRemote.executeCommand(url, user, pw, repoName, remoteName, remoteURL);
					// pull remote
					BeginTransaction beginTransaction = new BeginTransaction();
					GeogigTransaction transaction = beginTransaction.executeCommand(url, user, pw, initReposName);
					String transactionId = transaction.getTransaction().getId();
					PullRepository pull = new PullRepository();
					pull.executeCommand(url, user, pw, initReposName, transactionId, remoteName, "master", "master",
							authorName, authorEmail);
					EndTransaction endTransaction = new EndTransaction();
					endTransaction.executeCommand(url, user, pw, initReposName, transactionId);
				} catch (GeogigCommandException e) {
					JAXBContext jaxbContext = JAXBContext.newInstance(GeogigRepositoryInit.class);
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					geogigReposInit = (GeogigRepositoryInit) unmarshaller.unmarshal(new StringReader(e.getMessage()));

					DeleteRepository delete = new DeleteRepository();
					GeogigRepositoryDelete reposDelete = delete.executeGetCommand(url, user, pw, repoName);
					delete.executeDeleteCommand(url, user, pw, repoName, reposDelete.getToken());
				}
			}
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigRepositoryInit.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			geogigReposInit = (GeogigRepositoryInit) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		} catch (ResourceAccessException e) {
			geogigReposInit = new GeogigRepositoryInit();
			geogigReposInit.setError(
					"Exception during pool initialization: HikariPool-9 - Connection is not available, request timed out after 5000ms.");
			geogigReposInit.setSuccess("false");
		}
		return geogigReposInit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gitrnd.qaproducer.geogig.service.9GeogigRepositoryService#
	 * deleteRepository (com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager,
	 * java.lang.String)
	 */
	@Override
	public GeogigRepositoryDelete deleteRepository(DTGeoserverManager geoserverManager, String repoName)
			throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		DeleteRepository deleteReops = new DeleteRepository();
		GeogigRepositoryDelete geogigRepos = null;
		try {
			geogigRepos = deleteReops.executeGetCommand(url, user, pw, repoName);
			String token = geogigRepos.getToken();
			deleteReops.executeDeleteCommand(url, user, pw, repoName, token);
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigRepositoryDelete.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			geogigRepos = (GeogigRepositoryDelete) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return geogigRepos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gitrnd.qaproducer.geogig.service.GeogigRepositoryService#addRepository(
	 * com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public GeogigAdd addRepository(DTGeoserverManager geoserverManager, String repoName, String transactionId)
			throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		AddRepository addRepos = new AddRepository();
		GeogigAdd geogigAdd = null;
		try {
			geogigAdd = addRepos.executeCommand(url, user, pw, repoName, transactionId);
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigAdd.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			geogigAdd = (GeogigAdd) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return geogigAdd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gitrnd.qaproducer.geogig.service.GeogigRepositoryService#commitRepository
	 * (com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager, java.lang.String,
	 * java.lang.String, java.lang.String,
	 * com.gitrnd.qaproducer.common.security.LoginUser)
	 */
	@Override
	public GeogigCommit commitRepository(DTGeoserverManager geoserverManager, String repoName, String transactionId,
			String message, LoginUser loginUser) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		String authorName = loginUser.getUsername();
		String authorEmail = loginUser.getEmail();

		CommitRepository commitRepos = new CommitRepository();
		GeogigCommit geogigCommit = null;

		try {
			geogigCommit = commitRepos.executeCommand(url, user, pw, repoName, transactionId, message, authorName,
					authorEmail);
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigCommit.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			geogigCommit = (GeogigCommit) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return geogigCommit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gitrnd.qaproducer.geogig.service.GeogigRepositoryService#
	 * listRemoteRepository(com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager,
	 * java.lang.String, java.lang.Boolean)
	 */
	@Override
	public GeogigRemoteRepository listRemoteRepository(DTGeoserverManager geoserverManager, String repoName,
			Boolean verbose) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		ListRemoteRepository list = new ListRemoteRepository();
		GeogigRemoteRepository remotes = null;
		try {
			remotes = list.executeCommand(url, user, pw, repoName, verbose);
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigRemoteRepository.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			remotes = (GeogigRemoteRepository) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return remotes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gitrnd.qaproducer.geogig.service.GeogigRepositoryService#
	 * addRemoteRepository(com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public GeogigRemoteRepository addRemoteRepository(DTGeoserverManager geoserverManager, String repoName,
			String remoteName, String remoteURL, LoginUser loginUser) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		AddRemoteRepository add = new AddRemoteRepository();
		GeogigRemoteRepository remotes = null;
		try {
			remotes = add.executeCommand(url, user, pw, repoName, remoteName, remoteURL);
			BeginTransaction begin = new BeginTransaction();
			GeogigTransaction transaction = begin.executeCommand(url, user, pw, repoName);

			String transactionId = transaction.getTransaction().getId();
			String authorName = loginUser.getUsername();
			String authorEmail = loginUser.getEmail();

			// master pull
			PullRepository pull = new PullRepository();
			GeogigPull geogigPull = pull.executeCommand(url, user, pw, repoName, transactionId, remoteName, "master",
					"master", authorName, authorEmail);
			if (geogigPull.getPull() != null) {
				EndTransaction end = new EndTransaction();
				end.executeCommand(url, user, pw, repoName, transactionId);
			}
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigRemoteRepository.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(e.getMessage());
			remotes = (GeogigRemoteRepository) unmarshaller.unmarshal(reader);
			// delete remote
			RemoveRemoteRepository remove = new RemoveRemoteRepository();
			remove.executeCommand(url, user, pw, repoName, remoteName);
		}
		return remotes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gitrnd.qaproducer.geogig.service.GeogigRepositoryService#
	 * removeRemoteRepository(com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager,
	 * java.lang.String, java.lang.Boolean, java.lang.String)
	 */
	@Override
	public GeogigRemoteRepository removeRemoteRepository(DTGeoserverManager geoserverManager, String repoName,
			String remoteName) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		RemoveRemoteRepository remove = new RemoveRemoteRepository();
		GeogigRemoteRepository remotes = null;
		try {
			remotes = remove.executeCommand(url, user, pw, repoName, remoteName);
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigRemoteRepository.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			remotes = (GeogigRemoteRepository) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return remotes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gitrnd.qaproducer.geogig.service.GeogigRepositoryService#
	 * pingRemoteRepository(com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public GeogigRemoteRepository pingRemoteRepository(DTGeoserverManager geoserverManager, String repoName,
			String remoteName) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		PingRemoteRepository ping = new PingRemoteRepository();
		GeogigRemoteRepository remote = null;
		try {
			remote = ping.executeCommand(url, user, pw, repoName, remoteName);
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigRemoteRepository.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			remote = (GeogigRemoteRepository) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return remote;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gitrnd.qaproducer.geogig.service.GeogigRepositoryService#pullRepository(
	 * com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public GeogigPull pullRepository(DTGeoserverManager geoserverManager, String repoName, String transactionId,
			String remoteName, String branchName, String remoteBranchName, LoginUser loginUser) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		String authorName = loginUser.getUsername();
		String authorEmail = loginUser.getEmail();

		PullRepository pull = new PullRepository();
		GeogigPull geogigPull = null;
		try {
			geogigPull = pull.executeCommand(url, user, pw, repoName, transactionId, remoteName, branchName,
					remoteBranchName, authorName, authorEmail);
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigPull.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			geogigPull = (GeogigPull) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return geogigPull;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gitrnd.qaproducer.geogig.service.GeogigRepositoryService#pushRepository(
	 * com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public GeogigPush pushRepository(DTGeoserverManager geoserverManager, String repoName, String remoteName,
			String branchName, String remoteBranchName) throws JAXBException {

		String url = geoserverManager.getRestURL();
		String user = geoserverManager.getUsername();
		String pw = geoserverManager.getPassword();

		PushRepository push = new PushRepository();
		GeogigPush geogigPush = null;
		try {
			geogigPush = push.executeCommand(url, user, pw, repoName, remoteName, branchName, remoteBranchName);
		} catch (GeogigCommandException e) {
			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigPush.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			geogigPush = (GeogigPush) unmarshaller.unmarshal(new StringReader(e.getMessage()));
		}
		return geogigPush;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gitrnd.qaproducer.geogig.service.GeogigRepositoryService#fetchRepository(
	 * com.gitrnd.gdsbuilder.geoserver.DTGeoserverManager, java.lang.String)
	 */
	@Override
	public GeogigFetch fetchRepository(DTGeoserverManager geoserverManager, String repoName) throws JAXBException {

//		String url = geoserverManager.getRestURL();
//		String user = geoserverManager.getUsername();
//		String pw = geoserverManager.getPassword();
//
//		FetchRepository fetch = new FetchRepository();
//		GeogigFetch geogigFetch = null;
//		try {
//			geogigFetch = fetch.executeCommand(url, user, pw, repoName);
//		} catch (GeogigCommandException e) {
//			JAXBContext jaxbContext = JAXBContext.newInstance(GeogigFetch.class);
//			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//			geogigFetch = (GeogigFetch) unmarshaller.unmarshal(new StringReader(e.getMessage()));
//		}
//		return geogigFetch;

		return null;
	}

}
