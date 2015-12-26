/**
 * 
 */
package com.voizd.framework.success;

/**
 * @author Suresh
 * 
 */
public enum SuccessCodesEnum {

	COMMON_SUCCESS("SUCC_10001", "Operation completed successfully", "common.success"),
	
	// 11101 - USER SERVICE SUCCESS MESSAGES

	// 11201 - AUTHENTICATION SERVICE SUCCESS MESSAGES

	// 11301 - STATION SERVICE SUCCESS MESSAGES
	STATION_CREATE_SUCCESS("SUCC_11301", "Station creation is successfully done.", "station.create.success"),
	
	STATION_CREATE_ALREADY_EXIST("SUCC_11302", "Station is already exist in your list.", "station.already.success"),
	
	STATION_UPDATE_SUCCESS("SUCC_11303", "Station update is successfully done.", "station.update.success"),
	
	NO_STATION_SUCCESS("SUCC_11304", "Currently there is no station.", "no.station.success"),
	
	STATION_GET_SUCCESS("SUCC_11305", "Station get is successfully done.", "station.get.success"),
	
	TAP_SUCCESS("SUCC_11306", "Successfully done.", "tap.success"),
	
	LIKE_SUCCESS("SUCC_11307", "Successfully done.", "like.success"),
	
	MEDIA_CREATE_SUCCESS("SUCC_11308", "Media creation successfully done.", "media.create.success"),
	
	STATION_LIST_SUCCESS("SUCC_11309", "Station list is successfully delivered.", "station.get.list.success"),
	
	TAP_SELF_SUCCESS("SUCC_11311", "You are trying to tap self.", "tap.message"),
	
	NO_TAPPED_STATION_SUCCESS("SUCC_11310", "You have not tapped anyone till now.", "no.tapped.station.success"),
	
	NO_VOIZER_SUCCESS("SUCC_11311", "There is no top voizer till now.", "no.voizer.success"),
	
	AMPLIFY_SUCCESS("SUCC_11312", "You can see your amplify into your vStream.", "amplify.success"),
	
	AMPLIFIER_SUCCESS("SUCC_11313", "No Amplifier", "amplifier.success"),
	
	SELF_AMPLIFY_SUCCESS("SUCC_11314", "You can not amplify your own clip.", "amplify.success"),
	
	CONTENT_SUCCESS("SUCC_11315", "No Content", "content.success"),
	
	COMMENT_SUCCESS("SUCC_11316", "No Comment", "comment.success"),
	
	// 11401 - CLIP SERVICE SUCCESS MESSAGES
	CLIP_CREATE_SUCCESS("SUCC_11401", "Your thought has been voizd.", "clip.create.success"),
	
	CLIP_UPDATE_SUCCESS("SUCC_11402", "Clip update is successfully done.", "clip.update.success"),
	
	CLIP_DELETE_SUCCESS("SUCC_11403", "Clip deletion is successfully done.", "clip.delete.success"),
	
	CLIP_GET_SUCCESS("SUCC_11404", "Clip get is successfully done.", "clip.get.success"),
	
	CLIP_LIST_SUCCESS("SUCC_11405", "Clip list is successfully delivered.", "clip.get.list.success"),
	
	NO_CLIP_SUCCESS("SUCC_11406", "Currently there is no clip.", "no.clip.success"),
	
	NO_TAPPED_CLIP_SUCCESS("SUCC_11310", "You have not tapped any clip till now.", "no.tapped.clip.success"),
	
	// 11501 - STREAM SERVICE SUCCESS MESSAGES
	STREAM_CREATE_SUCCESS("SUCC_11501", "Stream creation is successfully done.", "stream.create.success"),
	
	STREAM_UPDATE_SUCCESS("SUCC_11502", "Stream update is successfully done.", "stream.update.success"),
	
	STREAM_DELETE_SUCCESS("SUCC_11503", "Stream deletion is successfully done.", "stream.delete.success"),
	
	STREAM_GET_SUCCESS("SUCC_11504", "Stream get is successfully done.", "stream.get.success"),
	
	STREAM_LIST_SUCCESS("SUCC_11505", "Stream list is successfully delivered.", "stream.get.list.success"),
	
	NO_STREAM_SUCCESS("SUCC_11506", "Currently there is no stream.", "no.stream.success"),
	
	STREAM_CREATE_ALREADY_EXIST("SUCC_11507", "Stream is already exist in your list.", "stream.already.success"),
	
	//stats
	STATS_SAVE_SUCCESS("SUCC_11601", "Stats saved successfully.", "stats.save.success"),
	
	// 11701 - SEARCHSERVICE SUCCESS MESSAGES
	SEARCH_SUCCESS("SUCC_11701", "There is no content available as per your search.", "search.content.success"),
	
	
	FORGET_PASSWORD_SUCCESS("SUCC_11801", "Email has been send to given E-mail address.Please check your E-mail.", "password.success"),
	
	// 11901 - CLIP SERVICE SUCCESS MESSAGES
	COMMENT_POST_SUCCESS("SUCC_11901", "Your thought has been voizd.", "comment.post.success"),

	COMMENT_DELETE_SUCCESS("SUCC_11902", "Comment deletion is successfully done.", "comment.delete.success"),
	
	NO_COMMENT_SUCCESS("SUCC_11903", "Currently there is no comments.", "no.comment.success"),
	
	
	
	;
	String successCode;
	String successMessage;
	String successLabelCode;

	SuccessCodesEnum(String successCode, String successMessage, String successLabelCode) {
		this.successCode = successCode;
		this.successMessage = successMessage;
		this.successLabelCode = successLabelCode;
	}

	/**
	 * @return the successCode
	 */
	public String getSuccessCode() {
		return successCode;
	}

	/**
	 * @param successCode
	 *            the successCode to set
	 */
	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}

	/**
	 * @return the successMessage
	 */
	public String getSuccessMessage() {
		return successMessage;
	}

	/**
	 * @param successMessage
	 *            the successMessage to set
	 */
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	/**
	 * @return the successLabelCode
	 */
	public String getSuccessLabelCode() {
		return successLabelCode;
	}

	/**
	 * @param successLabelCode
	 *            the successLabelCode to set
	 */
	public void setSuccessLabelCode(String successLabelCode) {
		this.successLabelCode = successLabelCode;
	}

	public static SuccessCodesEnum getSuccessCodesEnum(String successCode) {
		SuccessCodesEnum[] successCodesEnumArr = SuccessCodesEnum.values();
		for (SuccessCodesEnum successCodesEnum : successCodesEnumArr) {
			if (successCodesEnum.getSuccessCode().equalsIgnoreCase(successCode)) {
				return successCodesEnum;
			}
		}

		return COMMON_SUCCESS;
	}

}
