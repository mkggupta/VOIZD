package com.voizd.rest.helper.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.CommentVO;
import com.voizd.common.constant.ClientParamConstant;
import com.voizd.common.constant.VoizdCommentParam;

public class CommentHelperManager {
	private static Logger logger = LoggerFactory.getLogger(CommentHelperManager.class);
	
	public static CommentVO enrichCommentVO(HashMap<String, String> requestMap, Map<String, Object> clientMap){
		CommentVO  commentVO  = new CommentVO();
		
		if(null!=requestMap.get(VoizdCommentParam.CONTENT_ID)){
			commentVO.setContentId(Long.parseLong(requestMap.get(VoizdCommentParam.CONTENT_ID)));
		}
		if(null!=requestMap.get(VoizdCommentParam.COMMENT_TEXT)){
			commentVO.setCommentText(requestMap.get(VoizdCommentParam.COMMENT_TEXT));
		}
		if(null!=requestMap.get(VoizdCommentParam.FILEIDS)){
			commentVO.setFileIds(requestMap.get(VoizdCommentParam.FILEIDS));
		}
		
		if(null!=requestMap.get(VoizdCommentParam.APPIDS)){
			commentVO.setAppIds(requestMap.get(VoizdCommentParam.APPIDS));
		}
		 if(null!=clientMap.get(ClientParamConstant.LONGITUDE)){
			 commentVO.setLongitude(Float.parseFloat(clientMap.get(ClientParamConstant.LONGITUDE).toString()));
		 }
		 if(null!=clientMap.get(ClientParamConstant.LATITUDE)){
			 commentVO.setLatitude(Float.parseFloat(clientMap.get(ClientParamConstant.LATITUDE).toString()));
		 }
		 if(null!=clientMap.get(ClientParamConstant.COUNTRY)){
			 commentVO.setCountry(clientMap.get(ClientParamConstant.COUNTRY).toString());
		 }
		 if(null!=clientMap.get(ClientParamConstant.LANGUAGE)){
			 commentVO.setLanguage(clientMap.get(ClientParamConstant.LANGUAGE).toString());
		 }
		 if(null!=clientMap.get(ClientParamConstant.STATE)){
			 commentVO.setState(clientMap.get(ClientParamConstant.STATE).toString());
		 }
		 if(null!=clientMap.get(ClientParamConstant.CITY)){
			 commentVO.setCity(clientMap.get(ClientParamConstant.CITY).toString());
		 }
		 if(null!=clientMap.get(ClientParamConstant.LOCATION)){
			 commentVO.setLocation(clientMap.get(ClientParamConstant.LOCATION).toString());
		 }
		 if(null!=clientMap.get(ClientParamConstant.ADDREASS)){
			 commentVO.setAddress(clientMap.get(ClientParamConstant.ADDREASS).toString());
		 }
		 if(null!=requestMap.get(VoizdCommentParam.WEBLINK)){
			 commentVO.setWeblink(requestMap.get(VoizdCommentParam.WEBLINK));
		 }
		
		return commentVO;
	}
	
	public static CommentVO enrichUpdateCommentVO(HashMap<String, String> requestMap){
		CommentVO  commentVO  = new CommentVO();
		if(null!=requestMap.get(VoizdCommentParam.COMMENT_ID)){
			commentVO.setCommentId(Long.parseLong(requestMap.get(VoizdCommentParam.COMMENT_ID)));
		}
		
		return commentVO;
		
	}
	
	public static Long enrichCommentVO(HttpServletRequest httpServletRequest){
		Long id = null;
		if(httpServletRequest.getParameter(ClientParamConstant.CONTENT_ID) != null){
			id = Long.parseLong(httpServletRequest.getParameter(ClientParamConstant.CONTENT_ID));
		}
		return id;
	}
	
	
}
