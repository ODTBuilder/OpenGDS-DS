package com.gitrnd.gdsbuilder.geogig;

import org.springframework.http.HttpStatus;

/**
 * Geogig Command 오류 코드 Enum 클래스.
 * 
 * @author DY.Oh
 *
 */
public enum GeogigExceptionStatus {

	/**
	 * No transaction was specified, this command requires a transaction to preserve
	 * the stability of the repository.
	 */
	NO_TRANSACTION_WAS_SPECIFIED(HttpStatus.INTERNAL_SERVER_ERROR, "800",
			"No transaction was specified, this command requires a transaction to preserve the stability of the repository."),
	/**
	 * A transaction with the provided ID could not be found.
	 */
	TRANSACTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "801", "A transaction with the provided ID could not be found."),
	/**
	 * Repository not found.
	 */
	REPOSITORY_NOT_FOUND(HttpStatus.NOT_FOUND, "802", "Repository not found."),
	/**
	 * Invalid value specified for option.
	 */
	INVALID_OPTION(HttpStatus.NOT_FOUND, "803", "Invalid value specified for option."),
	/**
	 * Not a geogig command.
	 */
	INVALID_COMMAND(HttpStatus.NOT_FOUND, "804", "Not a geogig command."),
	/**
	 * Tried to start a transaction within a transaction.
	 */
	INVALID_TRANSACTION(HttpStatus.INTERNAL_SERVER_ERROR, "805", "Tried to start a transaction within a transaction."),
	/**
	 * Could not resolve branch or commit.
	 */
	BRANCH_OR_COMMIT_NOT_RESOLVE(HttpStatus.INTERNAL_SERVER_ERROR, "806", "Could not resolve branch or commit."),
	/**
	 * The supplied path does not resolve to a feature.
	 */
	PATH_NOT_FEATURE(HttpStatus.INTERNAL_SERVER_ERROR, "807", "The supplied path does not resolve to a feature."),
	/**
	 * The supplied path does not exist.
	 */
	PATH_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "808", "The supplied path does not exist."),
	/**
	 * Nothing to do.
	 */
	NOTHING_TO_DO(HttpStatus.INTERNAL_SERVER_ERROR, "809", "Nothing to do."),
	/**
	 * You must specify a valid non-null ObjectId.
	 */
	INVALID_OBJECT_ID(HttpStatus.INTERNAL_SERVER_ERROR, "810", "You must specify a valid non-null ObjectId."),
	/**
	 * The specified ObjectId was not found in the respository.
	 */
	OBJECT_ID_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "811",
			"The specified ObjectId was not found in the respository."),
	/**
	 * Repository has no HEAD, can't checkout.
	 */
	CHECKOUT_HEAD_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "812", "Repository has no HEAD, can't checkout."),
	/**
	 * Please specify either ours or theirs to update the feature path specified.
	 */
	PATH_CONFLICT(HttpStatus.INTERNAL_SERVER_ERROR, "813",
			"Please specify either ours or theirs to update the feature path specified."),
	/**
	 * No branch or commit specified for checkout.
	 */
	COMMITS_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "814", "No branch or commit specified for checkout."),
	/**
	 * You must specify the key when setting a config key.
	 */
	INVALID_CONFIG_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "815", "You must specify the key when setting a config key."),
	/**
	 * You must specify the value when setting a config key.
	 */
	INVALID_CONFIG_VALUE(HttpStatus.INTERNAL_SERVER_ERROR, "816",
			"You must specify the value when setting a config key."),
	/**
	 * Invalid old ref spec.
	 */
	INVALID_OLD_COMMIT(HttpStatus.INTERNAL_SERVER_ERROR, "817", "Invalid old ref spec."),
	/**
	 * Invalid path was specified.
	 */
	INVALID_PATH(HttpStatus.INTERNAL_SERVER_ERROR, "818", "Invalid path was specified."),
	/**
	 * Nothing specified to fetch from.
	 */
	NOTHING_TO_FETCH(HttpStatus.INTERNAL_SERVER_ERROR, "819", "Nothing specified to fetch from."),
	/**
	 * Unable to fetch, the remote history is shallow.
	 */
	FETCH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "820", "Unable to fetch, the remote history is shallow."),
	/**
	 * Couldn't resolve the given path.
	 */
	EMPTY_PATH(HttpStatus.INTERNAL_SERVER_ERROR, "821", "Couldn't resolve the given path."),
	/**
	 * Couldn't resolve the given path to a feature type.
	 */
	INVALID_FEATURETYPE(HttpStatus.INTERNAL_SERVER_ERROR, "822", "Couldn't resolve the given path to a feature type."),
	/**
	 * Repository has no HEAD, can't merge.
	 */
	MERGE_HEAD_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "823", "Repository has no HEAD, can't merge."),
	/**
	 * Couldn't resolve to a commit.
	 */
	MERGE_OBJECT_ID_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "824", "Couldn't resolve to a commit."),
	/**
	 * Unable to pull, the remote history is shallow.
	 */
	FULL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "825", "Unable to pull, the remote history is shallow."),
	/**
	 * Push failed: The remote repository has changes that would be lost in the
	 * event of a push.
	 */
	REMOTE_HAS_CHANGES(HttpStatus.INTERNAL_SERVER_ERROR, "826",
			"Push failed: The remote repository has changes that would be lost in the event of a push."),
	/**
	 * Push failed: There is not enough local history to complete the push.
	 */
	HISTORY_TOO_SHALLOW(HttpStatus.INTERNAL_SERVER_ERROR, "827",
			"Push failed: There is not enough local history to complete the push."),
	/**
	 * REMOTE_NOT_FOUND
	 */
	REMOTE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "828", "REMOTE_NOT_FOUND"),
	/**
	 * No URL was specified.
	 */
	INVALID_REMOTE_URL(HttpStatus.INTERNAL_SERVER_ERROR, "829", "No URL was specified."),
	/**
	 * Object ID could not be resolved to a feature.
	 */
	REVERT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "830", "Object ID could not be resolved to a feature."),
	/**
	 * Invalid reference.
	 */
	INVALID_REFERENCE(HttpStatus.INTERNAL_SERVER_ERROR, "831", "Invalid reference."),
	/**
	 * Parent tree couldn't be found in the repository.
	 */
	PARENT_TREE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "832", "Parent tree couldn't be found in the repository."),
	/**
	 * New commit id did not resolve to a valid tree.
	 */
	INVALID_NEW_COMMIT_ID(HttpStatus.INTERNAL_SERVER_ERROR, "833", "New commit id did not resolve to a valid tree."),
	/**
	 * Old commit id did not resolve to a valid tree.
	 */
	INVALID_OLD_COMMIT_ID(HttpStatus.INTERNAL_SERVER_ERROR, "834", "Old commit id did not resolve to a valid tree."),
	/**
	 * The feature was not found in either commit tree.
	 */
	FEATURE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "835", "The feature was not found in either commit tree."),
	/**
	 * No repository to delete.
	 */
	NO_REPOSITORY_TO_DELETE(HttpStatus.NOT_FOUND, "836", "No repository to delete."),
	/**
	 * Commit not found.
	 */
	COMMIT_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "837", "Commit not found."),
	/**
	 * Unable to resolve URI of newly created repository.
	 */
	INVALID_REPOSITORY_URL(HttpStatus.INTERNAL_SERVER_ERROR, "838",
			"Unable to resolve URI of newly created repository."),
	/**
	 * The request method is unsupported for this operation.
	 */
	UNSUPPORTED_METHOD(HttpStatus.INTERNAL_SERVER_ERROR, "839",
			"The request method is unsupported for this operation."),
	/**
	 * Unable to connect using the specified database parameters.
	 */
	CONNECTION_FAIL(HttpStatus.BAD_REQUEST, "840", "Unable to connect using the specified database parameters."),
	/**
	 * Unable to connect using the specified database parameters.
	 */
	ALREADY_INITIALIZED(HttpStatus.CONFLICT, "841", "Unable to connect using the specified database parameters."),
	/**
	 * Couldn't resolve commit's treeId
	 */
	INVALID_TREE_ID(HttpStatus.INTERNAL_SERVER_ERROR, "842", "Couldn't resolve commit's treeId"),
	/**
	 * Invalid POST data.
	 */
	INVALID_POST_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "843", "Invalid POST data."),
	/**
	 * You must specify the correct token to delete a repository.
	 */
	INVALID_DELETE_TOKEN(HttpStatus.BAD_REQUEST, "844", "You must specify the correct token to delete a repository."),
	/**
	 * The specified token does not exist or has expired.
	 */
	DELETE_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "845", "The specified token does not exist or has expired."),
	/**
	 * Can not set 'value' to 'true' with 'theirs' or 'ours' set.
	 */
	NOT_SET(HttpStatus.INTERNAL_SERVER_ERROR, "846", "Can not set 'value' to 'true' with 'theirs' or 'ours' set."),
	/**
	 * The specified repository name is already in use, please try a different name
	 */
	ALREADY_INITIALIZED_NAME(HttpStatus.CONFLICT, "847",
			"The specified repository name is already in use, please try a different name"),
	/**
	 * Unable to connect remote repository.
	 */
	REMOTE_CONNECTION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "848", "Unable to connect remote repository."),
	/**
	 * Connection refused
	 */
	GEOSERVER_CONNECTION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "849", "Connection refused"),
	/**
	 * Read timed out
	 */
	REPOSITORY_INITALIZE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "850", "Read timed out"),
	/**
	 * PSQLException: ERROR: schema
	 */
	PSQL_SCHEMA_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "851", "PSQLException: ERROR: schema"),
	/**
	 * REMOTE_ALREADY_EXISTS
	 */
	REMOTE_ALREADY_EXISTS(HttpStatus.CONFLICT, "852", "REMOTE_ALREADY_EXISTS"),
	/**
	 * java.io.FileNotFoundException
	 */
	FILE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "853", "java.io.FileNotFoundException"),
	/**
	 * java.net.UnknownHostException
	 */
	UNKNOWN_HOST(HttpStatus.INTERNAL_SERVER_ERROR, "854", "java.net.UnknownHostException"),
	/**
	 * Expected authority
	 */
	EXPECTED_AUTHORITY(HttpStatus.INTERNAL_SERVER_ERROR, "855", "Expected authority"),
	/**
	 * java.net.MalformedURLException
	 */
	MALFORMED_URL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "856", "java.net.MalformedURLException"),
	/**
	 * You need to resolve your index first.
	 */
	UNRESOLVE_CONFLICTS(HttpStatus.INTERNAL_SERVER_ERROR, "857", "You need to resolve your index first."),
	/**
	 * Illegal character in hostname
	 */
	ILLEGALCHARACTERINHOSTNAME(HttpStatus.INTERNAL_SERVER_ERROR, "858", "Illegal character in hostname"),
	/**
	 * No Commits Remote Repository
	 */
	NOCOMMITS(HttpStatus.INTERNAL_SERVER_ERROR, "859", "No Commits Remote Repository"),
	/**
	 * Could not read JSON
	 */
	COULDNOTREADJSON(HttpStatus.INTERNAL_SERVER_ERROR, "860", "Could not read JSON"),
	/**
	 * Premature EOF
	 */
	PREMATURE_EOF(HttpStatus.INTERNAL_SERVER_ERROR, "861", "Premature EOF");

	private HttpStatus httpStatus;
	private String status;
	private String message;

	GeogigExceptionStatus(HttpStatus httpStatus, String status, String message) {

		this.httpStatus = httpStatus;
		this.status = status;
		this.message = message;

	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * message에 해당하는 {@link GeogigExceptionStatus} 반환
	 * 
	 * @param message error message
	 * @return {@link GeogigExceptionStatus}
	 * 
	 * @author DY.Oh
	 */
	public static GeogigExceptionStatus getStatus(String message) {
		for (GeogigExceptionStatus status : values()) {
			if (message.contains(status.message)) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + message + "]");
	}

	/**
	 * message에 해당하는 {@link HttpStatus} 반환
	 * 
	 * @param message error message
	 * @return {@link HttpStatus}
	 * 
	 * @author DY.Oh
	 */
	public static HttpStatus getHTTPStatus(String message) {
		for (HttpStatus status : HttpStatus.values()) {
			if (message.contains(status.getReasonPhrase())) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + message + "]");
	}
}
