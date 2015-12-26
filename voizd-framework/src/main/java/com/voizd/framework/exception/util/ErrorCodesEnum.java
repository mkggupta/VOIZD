/**
 * 
 */
package com.voizd.framework.exception.util;

/**
 * @author Admin
 * 
 */
public enum ErrorCodesEnum {

	/*
	 * Error codes 10001 - 10999 are reserved for system exceptions
	 */
	SEVERE_ERROR("ERR_10001", "Severe Error", "common.error.severe.error"),

	SERVICE_UNAVAILABLE("ERR_10002", "Service Unavailable", "common.program.service.unavailable"),

	DATABASE_CONNECTION_UNAVAILABLE("ERR_10003", "Database Connection Error", "common.error.db.connection.unavailable"),

	UNEXPECTED_ERROR("ERR_10004", "Unexpected Error", "common.error.unexpected.error"),

	DATABASE_LAYER_EXCEPTION("ERR_10005", "Database Layer Exception", "common.error.db.exception"),

	OPERATION_NOT_IMPLEMENTED_EXCEPTION("ERR_10006", "Operation Not Supported Yet Exception", "common.error.operation.notimplemented"),

	AUTHENTICATION_FAILED_EXCEPTION("ERR_10007", "Authentication failed", "common.error.authentication.failed"),

	CONNECTION_ERROR("ERR_10008", "Connection Error", "common.error.connection.failed"),

	INVALID_REQUEST_EXCEPTION("ERR_10009", "Invalid request params", "common.validation.invalid.request.error"),

	DATABASE_UNIQUE_CONSTRAINT_VOILATION_EXCEPTION("ERR_10010", "Unique constraint voilation Exception",
			"common.error.db.unique.constraint.voilation.exception"),

	EMAIL_SENDING_FAILED_EXCEPTION("ERR_10011", "Email sending failed", "common.error.mail.sending.failed.exception"),
	
	/*
	 * Error codes 11001 need to be used for business exceptions
	 */
	//
	BUSINESS_RULES_SERVICE_FAILED_EXCEPTION("ERR11001", "Exception in fetching business rules", "com.voizd.bisrul.service.failed"),

	BUSINESS_RULE_NOT_FOUND("ERR11002", "No business rule found", "com.voizd.bisrul.not.found"),

	// USER SERVICE
	USER_SERVICE_FAILED_EXCEPTION("ERR11101", "Exception in user service", "com.voizd.user.service.failed"),

	USER_NOT_FOUND_EXCEPTION("ERR11102", "User not found in the system", "com.voizd.user.not.found"),

	USER_STATUS_INACTIVE("ERR11103", "User  status is inactive", "com.voizd.user.inactive"),

	USER_ALREADY_EXIST("ERR11104", "User already present", "com.voizd.user.already.exist"),

	USER_IS_BLOCKED("ERR11105", "User is blocked", "com.voizd.user.blocked"),

	ONLINE_STATUS_CHANGE_FAILED_EXCEPTION("ERR11106", "Exception in changing user online status", "com.voizd.user.online.status.change.failed"),

	USER_STATUS_UPDATE_FAILED_EXCEPTION("ERR11107", "Exception in updating status for the user", "com.voizd.user.status.update.failed"),
	
	USER_COUNTER_SERVICE_FAILED_EXCEPTION("ERR11108", "Exception in user counter service", "com.voizd.user.service.failed"),
	
	USER_NOT_EXIST_EXCEPTION("ERR11109", "Invalid username/password", "com.voizd.user.not.found"),
	
	USER_PUSH_UPDATE_EXCEPTION("ERR11110", "Invalid userid", "com.voizd.user.push.update"),


	// Station Service
	STATION_UPDATE_SERVICE_FAILED_EXCEPTION("ERR11201", "Exception in updating station", "com.voizd.service.station.bo.failed"),

	STATION_GET_SERVICE_FAILED_EXCEPTION("ERR11202", "Exception in fetching station", "com.voizd.service.station.bo.failed"),

	STATION_BUSINESS_RULES_FAILED_EXCEPTION("ERR11203", "station business rules does not support this", "com.voizd.service.station.bo.failed"),

	STATION_SERVICE_FAILED_EXCEPTION("ERR11204", "Exception in station service", "com.voizd.service.station.failed"),

	STATION_SERVICE_UPDATE_DATA_NOT_FOUND("ERR11205", "Exception in station service", "com.voizd.service.station.failed"),

	STATION_CREATION_FAILED_EXCEPTION("ERR11206", "Station creation is failed due to some reason.", "com.voizd.service.station.create.failed"),

	STATION_UPDATION_FAILED_EXCEPTION("ERR11207", "Station updation is failed due to some reason.", "com.voizd.service.station.update.failed"),

	STATION_GET_FAILED_EXCEPTION("ERR11208", "Station list is failed due to some reason.", "com.voizd.service.station.get.failed"),

	STATION_SHARE_FAILED_EXCEPTION("ERR11209", "Station share is failed due to some reason.", "com.voizd.service.station.share.failed"),
	
	STATION_FOLLOWER_GET_FAILED_EXCEPTION("ERR11210", "Followers list is failed due to some reason.", "com.voizd.service.station.follow.failed"),
	
	TOP_VOIZER_GET_FAILED_EXCEPTION("ERR11211", "Voizers list is failed due to some reason.", "com.voizd.service.voizer.list.failed"),

	// media service
	MEDIA_UPDATE_SERVICE_FAILED_EXCEPTION("ERR11301", "Exception in updating media business rules", "com.voizd.service.media.bo.failed"),

	MEDIA_CREATION_FAILED_EXCEPTION("ERR11302", "Media creation failed due to invalid data.", "com.voizd.service.media.create.failed"),

	MEDIA_GET_FAILED_EXCEPTION("ERR11303", "Media get failed due to some reason.", "com.voizd.service.media.get.failed"),
	// content Service
	STATION_CONTENT_UPDATE_SERVICE_FAILED_EXCEPTION("ERR11401", "Exception in updating content", "com.voizd.service.content.failed"),

	STATION_CONTENT_GET_SERVICE_FAILED_EXCEPTION("ERR11402", "Exception in fetching content", "com.voizd.service.content.failed"),

	STATION_CONTENT_SERVICE_FAILED_EXCEPTION("ERR11403", "Exception in content service", "com.voizd.service.content.failed"),

	STATION_CONTENT_UPDATE_FAILED_EXCEPTION("ERR11404", "Exception in update content service", "com.voizd.service.content.failed"),

	STATION_CONTENT_INVALID_USER("ERR11405", "Exception in invalid user updating content service", "com.voizd.service.content.failed"),

	STATION_CONTENT_DELETE_FAILED_EXCEPTION("ERR11406", "Exception in delete content service", "com.voizd.service.content.failed"),

	STATION_COUNTER_FAILED_EXCEPTION("ERR11407", "Exception in station counter service", "com.voizd.service.content.failed"),

	STATION_CONTENT_CREATION_FAILED_EXCEPTION("ERR11408", "Content creation failed due to some reason.", "com.voizd.service.content.failed"),

	STATION_CONTENT_UPDATION_FAILED_EXCEPTION("ERR11409", "Content updation failed due to some reason.", "com.voizd.service.content.failed"),

	STATION_CONTENT_GET_FAILED_EXCEPTION("EERR11410", "Content list failed due to some reason.", "com.voizd.service.content.failed"),

	STATION_CONTENT_SHARE_FAILED_EXCEPTION("ERR11411", "Content share failed due to some reason.", "com.voizd.service.content.share.failed"),
	
	STATION_CONTENT_DELETE_EXCEPTION("ERR11412", "Content has been removed by voizer", "com.voizd.service.content.delete.failed"),

	// Stream Service
	STREAM_SHARE_FAILED_EXCEPTION("ERR11701", "Stream share failed due to some reason.", "com.voizd.service.stream.share.failed"),

	STREAM_GET_FAILED_EXCEPTION("ERR11702", "Stream list failed due to some reason.", "com.voizd.service.stream.list.failed"),
	
	STREAM_UPDATE_SERVICE_FAILED_EXCEPTION("ERR11703", "Exception in updating stream", "com.voizd.service.stream.bo.failed"),
	
	STREAM_CREATION_FAILED_EXCEPTION("ERR11704", "Stream creation failed due to some reason.", "com.voizd.service.stream.create.failed"),

	STREAM_UPDATION_FAILED_EXCEPTION("ERR11705", "Stream updation failed due to some reason.", "com.voizd.service.stream.update.failed"),
	
	STREAM_UPDATE_DATA_NOT_FOUND("ERR11706", "Exception in stream service", "com.voizd.service.stream.failed"),
	
	STREAM_DELETE_FAILED_EXCEPTION("ERR11707", "Exception in delete stream service", "com.voizd.service.stream.failed"),
	
	// Stats Service

	CLIENT_STATS_SERVICE_FAILED_EXCEPTION("ERR11801", "Exception in stats service", "com.voizd.service.stats.failed"),
	
	//Earth Service 
	EARTH_SERVICE_GET_FAILED_EXCEPTION("ERR11802", "Tag list failed due to some reason.", "com.voizd.service.earth.failed"),

	// User Action Service
	USER_ACTION_UPDATE_SERVICE_FAILED_EXCEPTION("ERR11601", "Exception in updating user action ", "com.voizd.service.user.action.failed"),

	USER_ACTION_GET_SERVICE_FAILED_EXCEPTION("ERR11602", "Exception in fetching user action ", "com.voizd.service.user.action.failed"),

	USER_ACTION_FAILED_EXCEPTION("ERR11603", "user action  does not support this", "com.voizd.service.user.action.failed"),

	USER_ACTION_SERVICE_FAILED_EXCEPTION("ERR11604", "Exception in user action service", "com.voizd.service.user.action.failed"),

	USER_TAP_SERVICE_FAILED_EXCEPTION("ERR11605", "Action failed due to some reason.", "com.voizd.service.user.action.tap.failed"),

	USER_LIKE_SERVICE_FAILED_EXCEPTION("ERR11606", "Action failed due to some reason.", "com.voizd.service.user.action.like.failed"),

	OLD_PASSWORD_IN_CORRECT("ERR11607", "Old password is incorrect.Please enter correct password.", "common.error.validation.old.password.incorrect"),
	
	USER_DOB_INCORRECT("ERR11608", "User DOB is incorrect.User must be 13 year old.", "common.error.validation.dob.incorrect"),
	
	USER_ACTION_DATA_NOT_FOUND_EXCEPTION("ERR11609", "Correct data not found ", "com.voizd.service.user.action.failed"),
	
	USER_ACCOUNT_INACTIVE("ERR11610", "User is not active currently", "com.voizd.service.user.action.failed"),
	
	USER_AMPLIFY_SERVICE_FAILED_EXCEPTION("ERR11611", "Trying to amplify same content again.", "com.voizd.service.amplify.action.failed"),
	
	USER_AMPLIFIER_SERVICE_FAILED_EXCEPTION("ERR11612", "Could not fetch amplifier list currently.", "com.voizd.service.amplifier.action.failed"),
	
	USER_COMMENT_SERVICE_FAILED_EXCEPTION("ERR11613", "Could not fetch comment list currently.", "com.voizd.service.comment.action.failed"),
	
	// Authentication Service
	AUTHENTICATION_SERVICE_FAILED_EXCEPTION("ERR11501", "Exception in authentication service", "com.voizd.authentication.service.failed"),

	INVALID_EMAIL_VERIFICATION_CREDENTIALS_EXCEPTION("ERR11502", "Invalid email verification credentials", "com.voizd.email.verification.credential.invalid"),

	LINK_EXPIRED_EXCEPTION("ERR11503", "Link expired", "com.voizd.link.expired"),

	INVALID_PASSWORD_VERIFICATION_CREDENTIALS_EXCEPTION("ERR11504", "Invalid password verification credentials",
			"com.voizd.password.verification.credential.invalid"),

	INVALID_AUTH_KEY_EXCEPTION("ERR11505", "Invalid auth key", "com.voizd.authkey.verification.credential.invalid"),

	INVALID_REQUEST_TYPE_EXCEPTION("ERR11506", "Invalid request type", "com.voizd.requesttype.invalid"),
	
	//Search service 
	SEARCH_SERVICE_FAILED_EXCEPTION("ERR12001", "Exception in Search service", "com.voizd.search.service.failed"),
	
	//advt service // comment
	
	ADVT_SERVICE_FAILED_EXCEPTION("ERR11901", "Exception in advt service", "com.voizd.adv.service.failed"),
	
	COMMENT_SERVICE_FAILED_EXCEPTION("ERR11902", "Exception in comment service", "com.voizd.comment.service.failed"),
	
	COMMENT_DELETE_FAILED_EXCEPTION("ERR11903", "Exception in delete comment service", "com.voizd.service.comment.failed"),

	// Request Log

	REQUEST_LOG_SERVICE_FAILED_EXCEPTION("ERR11603", "Exception in request log service", "com.voizd.requestlog.service.failed"),

	

	// VAlidation exceptions
	/*
	 * Error codes 11001 need to be used for business exceptions
	 */
	//
	USERNAME_MISSING("ERR21001", "Username is missing", "common.error.validation.username.missing"),

	PASSWORD_MISSING("ERR21002", "Password is missing", "common.error.validation.password.missing"),

	LOGIN_MODE_MISSING("ERR21003", "Login mode is missing", "common.error.validation.loginMode.missing"),

	APP_ID_MISSING("ERR21004", "App Id is missing", "common.error.validation.appId.missing"),

	PARTNER_USER_KEY_MISSING("ERR21005", "Partner user key is missing", "common.error.validation.partnerUserKey.missing"),

	USER_SERVICE_VALIDATION_FAILED_EXCEPTION("ERR21006", "Exception in user service validation", "common.error.validation.user.service"),

	FIRST_NAME_MISSING("ERR21007", "First name is missing", "common.error.validation.firstname.missing"),
	
	USER_GENDER_MISSING("ERR21008", "Gender is missing", "common.error.validation.gender.missing"),

	FORGOT_PASSWORD_ID_MISSING("ERR21008", "Forgot password id is missing", "common.error.validation.forgotPasswordId.missing"),

	USERID_MISSING("ERR21009", "UserId is missing", "common.error.validation.userId.missing"),

	VERIFICTION_CODE_MISSING("ERR21010", "Verification code is missing", "common.error.validation.verificationCode.missing"),
	
	USER_DOB_MISSING("ERR21011", "User DOB is missing", "common.error.validation.dob.missing"),
	
	STATS_DATA_MISSING("ERR21101", "Stats data is missing", "common.error.validation.stats.data.missing"),

	OLD_PASSWORD_MISSING("ERR21102", "Old Password is missing", "common.error.validation.old.password.missing"),
	
	USER_PASSWORD_WRONG("ERR21103", "The username or password you entered is incorrect.", "common.error.login.fail"),

	;

	String errorCode;
	String errorMessage;
	String errorLabelCode;

	ErrorCodesEnum(String errorCode, String errorMessage, String errorLabelCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorLabelCode = errorLabelCode;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the errorLabelCode
	 */
	public String getErrorLabelCode() {
		return errorLabelCode;
	}

	/**
	 * @param errorLabelCode
	 *            the errorLabelCode to set
	 */
	public void setErrorLabelCode(String errorLabelCode) {
		this.errorLabelCode = errorLabelCode;
	}

	public static ErrorCodesEnum getErrorCodesEnum(String errorCode) {
		ErrorCodesEnum[] errorCodesEnumArr = ErrorCodesEnum.values();
		for (ErrorCodesEnum errorCodesEnum : errorCodesEnumArr) {
			if (errorCodesEnum.getErrorCode().equalsIgnoreCase(errorCode)) {
				return errorCodesEnum;
			}
		}

		return UNEXPECTED_ERROR;
	}

}
