package com.voizd.service.content.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.TapClipVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.util.DateTimeUtils;


public class ContentUtils {
	private static Logger logger = LoggerFactory.getLogger(ContentUtils.class);
	public static TapClipVO transformTapClipVO(ContentVO contentVO,UserVO userVO,int userStatus){		
		TapClipVO tapClipVO = null;
		if(contentVO != null){
			tapClipVO = new TapClipVO();
			tapClipVO.setStationId(contentVO.getStationId());
			tapClipVO.setContentId(contentVO.getContentId());
			tapClipVO.setStatus(contentVO.getStatus());
			tapClipVO.setCategory(contentVO.getCategory());
			tapClipVO.setTitle(contentVO.getTitle());
			tapClipVO.setTag(contentVO.getTag());
			tapClipVO.setLanguage(contentVO.getLanguage());
			tapClipVO.setLocation(contentVO.getLocation());
			tapClipVO.setLongitude(contentVO.getLongitude());
			tapClipVO.setLatitude(contentVO.getLatitude());
			tapClipVO.setCity(contentVO.getCity());
			tapClipVO.setState(contentVO.getState());
			tapClipVO.setCountry(contentVO.getCountry());
			tapClipVO.setWeblink(contentVO.getWeblink());
			//tapClipVO.setCreatedDate(contentVO.getCreatedDate());
			try {
				tapClipVO.setCreatedDate(DateTimeUtils.getGMTDate());
			} catch (Exception e) {
				logger.error("Exception while get date "+e.getLocalizedMessage(),e);
			}
			
			tapClipVO.setCreatorId(contentVO.getCreatorId());
			tapClipVO.setFileIds(contentVO.getFileIds());
			tapClipVO.setAddress(contentVO.getAddress());
			if(contentVO.getRegistration() != null && "yes".equalsIgnoreCase(contentVO.getRegistration())){
			  tapClipVO.setRegistration(true);
			}
			tapClipVO.setUsername(userVO.getPrimaryEmailAddress());
			tapClipVO.setUserFileId(userVO.getProfilePicFileId());
			tapClipVO.setUserStatus((byte)userStatus);
			}
		return tapClipVO;
	}

}
