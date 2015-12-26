package com.voizd.media.utils;

import com.voizd.media.dataobject.MediaCategory;

public class Destinations {
		private static final String COMMUNITY="g:";
		private static final String COMMUNITY_AGENT="a:community";
		private static final String USER_AGENT="a:usermgr";
		private static final String GALLERY_AGENT="a:ugcadmin";
		private static final String COMMENT="a:commentmgr";
		private static final String CONTEST="a:contestmgr";
		private static final String AGENT="a:";
		
		public static String getCategory(String destination){
			destination=destination.toLowerCase();
			if(destination.contains(COMMUNITY)){
				return MediaCategory.COMMUNITY.getValue();
			}else if(destination.contains(COMMUNITY_AGENT)){
				return MediaCategory.COMMUNITY_PROFILE.getValue();
			}else if(destination.contains(USER_AGENT)){
				return MediaCategory.USER.getValue();
			}else if(destination.contains(GALLERY_AGENT)){
				return MediaCategory.GALLLERY.getValue();
			}else if(destination.contains(COMMENT)){
				return MediaCategory.COMMENT.getValue();
			}else if(destination.contains(CONTEST)){
				return MediaCategory.CONTEST.getValue();
			}else{
				return MediaCategory.P2P.getValue();
			}
		 }
		
		public static enum UserMessageType {
			IM("IM"),
			PPMSG("PPMSG"),
			COMMSG("COMMSG"),
			AGTMSG("AGTMSG"),
			NOTFY("NOTFY"),
			FRNDIN("FRNDIN"),
			TICKER("TICKER"),
			BUZZ("BUZZ"),
			EXTRA("EXTRA"),
			DEFAULT("DEFAULT"),
			OTHER("OTHER");
			
			private String value;
			public String getValue() {
				return value;
			}
			private UserMessageType(String value){
				this.value = value;
			}
			
		}


		public static UserMessageType getMessageType(String destination){
			if(destination == null ) return UserMessageType.DEFAULT;
			UserMessageType userMessageType = UserMessageType.PPMSG;
			destination=destination.toLowerCase();
			if(destination.contains(COMMUNITY)){
				userMessageType = UserMessageType.COMMSG;
			}else if(destination.contains(AGENT)){
				userMessageType = UserMessageType.EXTRA;
			}else{
				userMessageType = UserMessageType.PPMSG;
			}
			return userMessageType;
		 }
}
