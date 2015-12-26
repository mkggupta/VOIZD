package com.voizd.util;

public class PushMessageUtil {
	
	public static String  getPushMessage(String title, String messageText, String firstName,String lastName){
		String message =null;
		if(lastName != null){
			 message = firstName +" "+lastName+ " has "+messageText+" your post,"+title;
		}else{
			message = firstName + " has "+messageText+" your post,"+title;	
		}
		return message;
	}
   
}
