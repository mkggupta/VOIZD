package com.voizd.common.constant;

public interface VoizdConstant {
	Byte RECENT_STATION_STATUS = 0;
	Byte POPULAR_STATION_STATUS = 1;
	Byte MY_TAP_STATION_STATUS = 2;
	Byte MY_STATION_STATUS = 3;
	Byte STATION_FOLLOW_STATUS = 0;
	Byte STATION_UNFOLLOW_STATUS = 1;
	Byte STATION_LIKE_STATUS = 0;
	Byte STATION_UNLIKE_STATUS = 1;
	Byte STATION_ACTIVE_STATUS = 1;
	Byte STATION_DEACTIVE_STATUS = 0;
	Byte STATION_DELETED_STATUS = 2;
	Byte MEDIA_ACTIVE_STATUS = 1;
	Byte RECENT_CONTENT_STATUS = 0;
	Byte POPULAR_CONTENT_STATUS = 1;
	Byte MY_TAP_CONTENT_STATUS = 2;
	Byte MY_CONTENT_STATUS = 3;
	Byte CONTENT_FOLLOW_STATUS = 0;
	Byte CONTENT_UNFOLLOW_STATUS = 1;
	Byte CONTENT_LIKE_STATUS = 0;
	Byte CONTENT_UNLIKE_STATUS = 1;
	Byte CONTENT_ACTIVE_STATUS = 1;
	Byte CONTENT_DELETED_STATUS = 5;
	Byte COMMENT_ACTIVE_STATUS = 1;
	Byte COMMENT_DELETED_STATUS = 5;
	Byte ADULT_STATUS = 1;
	Byte NOT_ADULT_STATUS = 0;
	Byte DEFAULT_STATION_STATUS= 0;
	Byte ACTIVE_STATION_STATUS= 1;
	Byte DELETE_STATION_STATUS= 2;
	String RECENT = "recent";
	String POPULAR = "popular";
	String MY = "my";
	String MYTAPPED = "mytapped";
	String MYBOOKMARK = "bookmarked";
	String MYSTREAM = "mystream";
	String DESCENDING = "0";
	String ASCENDING = "1";
	String USERID = "userId";
	String STATION = "station";
	String CONTENT = "content";
	String STREAM = "stream";
	String IMAGE = "image";
	String AUDIO = "audio";
	Byte FACEBOOK_APPID = 1;
	Byte GOOGLEPLUS_APPID = 2;
	Byte TWITTER_APPID = 3;
	Byte TUMBLR_APPID = 4;
	Byte WHATSAPP_APPID = 5;
	String THUMB_URL = "thumburl";
	String SHARE_URL = "shareurl";
	String MESSAGE = "message";
	String MY_STATION_STREAM = "stationstream";
	String MY_CLIP_STREAM = "clipstream";
	String CLIENT_APP_STATS = "app";
	String CLIENT_STATS = "appclient";
	String APP_STATS_KEY_HEADER = "stats";
	String CLIENT_STATS_KEY_HEADER = "clientstats";
	String voizd_REQ_PARAMS_MAP = "vzRequestParamMap";
	String STATION_MAP = "vzStationParamMap";
	String STATION_RESULT_SIZE = "vzStationResultSize";
	String DEFAULT_PLATFORM = "iphone";
	String ANDROID_PLATFORM = "andriod";
	String MP3 = "mp3";
	String AMR = "amr";
	String voizd_CLIENT_PROPERTY = "clientproperty";
	String CONTENT_MAP = "vzContentParamMap";
	String CONTENT_RESULT_SIZE = "vzContentResultSize";
	String voizd_CLIENT_PARAM_SEPERATOR = "::";
	String voizd_CLIENT_KEY_SEPERATOR = "##";
	String STREAM_SHARE_URL_KEY  ="streamShareUrl";
	String DEFAULT_FILE_NAME ="station";
	String DEFAULT_PROFILE_FILE_NAME ="station";
	String SUCCESS ="success";
	String STATUS ="status";
	String USER_NAME="username";
	String DEFAULT_STREAM_NAME ="my private";
	String DEFAULT_STREAM_DESCRIPTION ="my private stream";
	Byte ACTIVE_STREAM_STATUS= 1;
	Byte DEACTIVE_STREAM_STATUS= 0;
	Byte DELETED_STREAM_STATUS=2;
	Byte PRIVATE_STREAM_STATUS=0;
	Byte PUBLIC_STREAM_STATUS= 1;
	Byte ALL_STREAM_STATUS =3;
	Byte STREAM_LIKE_STATUS = 0;
	Byte STREAM_UNLIKE_STATUS = 1;
	String IMAGE_JPG ="jpg";
	Byte STATION_START_LIMIT=0;
	Byte STATION_END_LIMIT=1;
	Byte ACTIVE_AMPLIFY_STATUS= 0;
	Byte INACTIVE_AMPLIFY_STATUS=1;
	String DEFAULT_STATION_NAME ="my private station";
	String DEFAULT_STATION_DESCRIPTION ="my private station";
	String DEFAULT_COUNTRY="United States";
	String DEFAULT_CITY="US";
	String DEFAULT_STATE="US";
	String DEFAULT_LANGUAGE="English";
	String DEFAULT_MALE="male";
	String DEFAULT_FEMALE="female";
	String DEFAULT_SHARE="share";
	String MYFOLLOWER= "myfollower";
	String TOPVOIZER= "topvoizer";
	String AMPLIFIER= "amplifier";
	String IFOLLOW= "ifollow";
	String ADRESS_SEPERATOR=",";
	int ACTIVE_USER_STATUS= 1;
	String VISITOR_ID="vId";
	String GRID_COUNT="3";
	int DEFAULT_GRID_TAG_COUNT=9;
	String INDIA="india";
	String UNITED_STATES="United States";
	String HAS_NEXT = "1";
	String GLOBE_PART_ONE="ONE";
	String GLOBE_PART_TWO="TWO";
	String INDIA_CODE="IND";
	String US_CODE="US";
	String TAG_URL="tagUrl";
	String LOCATION_URL="locationUrl";
	String AUDIO_URL="audioUrl";
	String GRID="grid";
	String GRID_HOME="gridehome";
	String MAP_VIEW="mapView";
	String TAG_SEPERATOR=",";
	String ALL="all";
	String COUNT="count";
	String AMPLIFY_KEY="amplify";
	String COMMAND_KEY="command";
	String AMPLIFY_COMMAND="amplify";
	String DEAMPLIFY_COMMAND="deAmplify";
	String CLIP_KEY ="content";
	String PROFILE_URL="profileUrl";
}
