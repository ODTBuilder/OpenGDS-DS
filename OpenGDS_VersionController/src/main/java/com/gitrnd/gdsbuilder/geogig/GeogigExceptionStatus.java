package com.gitrnd.gdsbuilder.geogig;

import org.springframework.http.HttpStatus;

/**
 * Geogig Command 오류 코드 Enum 클래스.
 * 
 * @author DY.Oh
 *
 */
public enum GeogigExceptionStatus {

	NO_TRANSACTION_WAS_SPECIFIED(HttpStatus.INTERNAL_SERVER_ERROR, "800",
			"No transaction was specified, this command requires a transaction to preserve the stability of the repository."),

	TRANSACTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "801", "A transaction with the provided ID could not be found."),

	REPOSITORY_NOT_FOUND(HttpStatus.NOT_FOUND, "802", "Repository not found."),

	INVALID_OPTION(HttpStatus.NOT_FOUND, "803", "Invalid value specified for option."),

	INVALID_COMMAND(HttpStatus.NOT_FOUND, "804", "Not a geogig command."),

	INVALID_TRANSACTION(HttpStatus.INTERNAL_SERVER_ERROR, "805", "Tried to start a transaction within a transaction."),

	BRANCH_OR_COMMIT_NOT_RESOLVE(HttpStatus.INTERNAL_SERVER_ERROR, "806", "Could not resolve branch or commit."),

	PATH_NOT_FEATURE(HttpStatus.INTERNAL_SERVER_ERROR, "807", "The supplied path does not resolve to a feature."),

	PATH_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "808", "The supplied path does not exist."),

	NOTHING_TO_DO(HttpStatus.INTERNAL_SERVER_ERROR, "809", "Nothing to do."),

	INVALID_OBJECT_ID(HttpStatus.INTERNAL_SERVER_ERROR, "810", "You must specify a valid non-null ObjectId."),

	OBJECT_ID_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "811",
			"The specified ObjectId was not found in the respository."),

	CHECKOUT_HEAD_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "812", "Repository has no HEAD, can't checkout."),

	PATH_CONFLICT(HttpStatus.INTERNAL_SERVER_ERROR, "813",
			"Please specify either ours or theirs to update the feature path specified."),

	COMMITS_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "814", "No branch or commit specified for checkout."),

	INVALID_CONFIG_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "815", "You must specify the key when setting a config key."),

	INVALID_CONFIG_VALUE(HttpStatus.INTERNAL_SERVER_ERROR, "816",
			"You must specify the value when setting a config key."),

	INVALID_OLD_COMMIT(HttpStatus.INTERNAL_SERVER_ERROR, "817", "Invalid old ref spec."),

	INVALID_PATH(HttpStatus.INTERNAL_SERVER_ERROR, "818", "Invalid path was specified."),

	NOTHING_TO_FETCH(HttpStatus.INTERNAL_SERVER_ERROR, "819", "Nothing specified to fetch from."),

	FETCH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "820", "Unable to fetch, the remote history is shallow."),

	EMPTY_PATH(HttpStatus.INTERNAL_SERVER_ERROR, "821", "Couldn't resolve the given path."),

	INVALID_FEATURETYPE(HttpStatus.INTERNAL_SERVER_ERROR, "822", "Couldn't resolve the given path to a feature type."),

	MERGE_HEAD_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "823", "Repository has no HEAD, can't merge."),

	MERGE_OBJECT_ID_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "824", "Couldn't resolve to a commit."),

	FULL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "825", "Unable to pull, the remote history is shallow."),

	REMOTE_HAS_CHANGES(HttpStatus.INTERNAL_SERVER_ERROR, "826",
			"Push failed: The remote repository has changes that would be lost in the event of a push."),

	HISTORY_TOO_SHALLOW(HttpStatus.INTERNAL_SERVER_ERROR, "827",
			"Push failed: There is not enough local history to complete the push."),

	REMOTE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "828", "REMOTE_NOT_FOUND"),

	INVALID_REMOTE_URL(HttpStatus.INTERNAL_SERVER_ERROR, "829", "No URL was specified."),

	REVERT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "830", "Object ID could not be resolved to a feature."),

	INVALID_REFERENCE(HttpStatus.INTERNAL_SERVER_ERROR, "831", "Invalid reference."),

	PARENT_TREE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "832", "Parent tree couldn't be found in the repository."),

	INVALID_NEW_COMMIT_ID(HttpStatus.INTERNAL_SERVER_ERROR, "833", "New commit id did not resolve to a valid tree."),

	INVALID_OLD_COMMIT_ID(HttpStatus.INTERNAL_SERVER_ERROR, "834", "Old commit id did not resolve to a valid tree."),

	FEATURE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "835", "The feature was not found in either commit tree."),

	NO_REPOSITORY_TO_DELETE(HttpStatus.NOT_FOUND, "836", "No repository to delete."),

	COMMIT_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "837", "Commit not found."),

	INVALID_REPOSITORY_URL(HttpStatus.INTERNAL_SERVER_ERROR, "838",
			"Unable to resolve URI of newly created repository."),

	UNSUPPORTED_METHOD(HttpStatus.INTERNAL_SERVER_ERROR, "839",
			"The request method is unsupported for this operation."),

	CONNECTION_FAIL(HttpStatus.BAD_REQUEST, "840", "Unable to connect using the specified database parameters."),

	ALREADY_INITIALIZED(HttpStatus.CONFLICT, "841", "Unable to connect using the specified database parameters."),

	INVALID_TREE_ID(HttpStatus.INTERNAL_SERVER_ERROR, "842", "Couldn't resolve commit's treeId"),

	INVALID_POST_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "843", "Invalid POST data."),

	INVALID_DELETE_TOKEN(HttpStatus.BAD_REQUEST, "844", "You must specify the correct token to delete a repository."),

	DELETE_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "845", "The specified token does not exist or has expired."),

	NOT_SET(HttpStatus.INTERNAL_SERVER_ERROR, "846", "Can not set 'value' to 'true' with 'theirs' or 'ours' set."),

	ALREADY_INITIALIZED_NAME(HttpStatus.CONFLICT, "847",
			"The specified repository name is already in use, please try a different name"),

	REMOTE_CONNECTION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "848", "Unable to connect remote repository."),

	GEOSERVER_CONNECTION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "849", "Connection refused"),

	REPOSITORY_INITALIZE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "850", "Read timed out"),

	PSQL_SCHEMA_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "851", "PSQLException: ERROR: schema"),

	REMOTE_ALREADY_EXISTS(HttpStatus.CONFLICT, "852", "REMOTE_ALREADY_EXISTS"),

	FILE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "853", "java.io.FileNotFoundException"),

	UNKNOWN_HOST(HttpStatus.INTERNAL_SERVER_ERROR, "854", "java.net.UnknownHostException"),

	EXPECTED_AUTHORITY(HttpStatus.INTERNAL_SERVER_ERROR, "855", "Expected authority"),

	MALFORMED_URL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "856", "java.net.MalformedURLException"),

	UNRESOLVE_CONFLICTS(HttpStatus.INTERNAL_SERVER_ERROR, "857", "You need to resolve your index first."),

	ILLEGALCHARACTERINHOSTNAME(HttpStatus.INTERNAL_SERVER_ERROR, "858", "Illegal character in hostname"),

	NOCOMMITS(HttpStatus.INTERNAL_SERVER_ERROR, "859", "No Commits Remote Repository"),

	COULDNOTREADJSON(HttpStatus.INTERNAL_SERVER_ERROR, "860", "Could not read JSON"),

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
