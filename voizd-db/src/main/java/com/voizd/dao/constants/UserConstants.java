package com.voizd.dao.constants;

public interface UserConstants {

	// QUERY IDS
	String QUERY_GET_USER_INFO = "get_user_info";
	String QUERY_GET_USER_AUTH_DETAILS = "get_user_auth_details";
	String QUERY_GET_USER_AUTH_DETAILS_BY_ID = "get_user_auth_details_by_id";
	String QUERY_GET_USER_STATUS_BY_ID = "get_user_status_by_id";
	String QUERY_INSERT_USER_INFO = "insert_user_info";
	String QUERY_UPDATE_USER_INFO = "update_user_info";

	String QUERY_INSERT_USER_AUTH_DETAILS = "insert_user_auth_details";
	String QUERY_INSERT_USER_THIRD_PARTY_AUTH_DETAILS = "insert_user_third_party_auth_details";
	String QUERY_GET_USER_THIRD_PARTY_AUTH_DETAILS = "get_user_third_party_auth_details";
	String QUERY_GET_USER_AUTH_DETAILS_BY_USER_KEY = "get_auth_details_by_userkey";
	String QUERY_GET_USER_AUTH_DETAILS_BY_ALL = "get_auth_details";
	String QUERY_UPDATE_USER_LOGIN_STATUS = "update_user_online_status";
	String QUERY_UPDATE_USER_PASSWORD = "update_user_password";
	String QUERY_GET_EMAIL_VERIFICATION_DETAILS_BY_ID = "get_email_verification_details_by_id";
	String QUERY_UPDATE_USER_STATUS = "update_user_status";
	String QUERY_INSERT_EMAIL_VERIFICATION_DETAILS = "insert_email_verification_details";
	String QUERY_INSERT_FORGET_PASSWORD_VERIFICATION_DETAILS = "insert_forget_password_verification_details";
	String QUERY_GET_FORGOT_PASSWORD_VERIFICATION_DETAILS_BY_ID = "get_forget_password_verification_details_by_id";
	String QUERY_UPDATE_USER_PASSWORD_BY_ID = "update_user_password_by_id";
	String QUERY_UPDATE_USER_LOGIN_PARAMS = "update_user_login_params";
	String QUERY_UPDATE_USER_LOGIN_PARAMS_BY_ID = "update_user_login_params_by_id";
	String QUERY_UPDATE_FORGOT_PASSWORD_VERIFICATION_DETAILS = "update_forget_password_verification_details";

	String QUERY_INSERT_REQUEST_LOG = "insert_request_log";

	String QUERY_UPDATE_USER_PUSH_MESSAGE_STATUS = "update_user_push_message_status";
	String QUERY_UPDATE_USER_PUSH_STATUS_WITH_PUSH_KEY = "update_user_push_status_with_push_key";
	String QUERY_INSERT_USER_PUSH_INFO = "insert_user_push_info";
	String QUERY_GET_USER_PUSH_INFO = "get_user_push_info";
	
	String COLUMN_ID = "id";
	String COLUMN_USER_NAME = "username";
	String COLUMN_THIRD_PARTY_ID = "thirdPartyId";
	String COLUMN_THIRD_PARTY_USER_KEY = "userKey";
	String COLUMN_THIRD_PARTY_APP_KEY = "appKey";
	String COLUMN_LOGIN_STATUS = "loginStatus";
	String COLUMN_PASSWORD = "password";
	String COLUMN_USER_STATUS = "status";
	String COLUMN_CURRENT_CLIENT_VERSION = "currentClientVersion";
	String COLUMN_CURRENT_PLATFORM = "currentPlatform";
	String COLUMN_STATUS = "status";
	String PUSH_KEY = "pushKey";
	String USER_ID="user_id";
	String SEND_NOTIFICATION="send_notif";
	String PLATFORM="platform";
}
