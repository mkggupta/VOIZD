package com.voizd.rest.controller.comment;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.voizd.common.beans.vo.CommentVO;
import com.voizd.common.constant.VoizdStationContentParam;
import com.voizd.common.util.ClientHeaderUtil;
import com.voizd.common.util.ControllerUtils;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.framework.success.SuccessCodesEnum;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.rest.helper.manager.CommentHelperManager;
import com.voizd.service.comment.exception.CommentServiceException;
import com.voizd.service.comment.v1_0.CommentService;
@Controller
@RequestMapping("/api/comment")
public class CommentController {
	private static Logger logger = LoggerFactory.getLogger(CommentController.class);
    private CommentService commentService ;
	

	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	@RequestMapping(value = "/vzpr/commentpost", method = {RequestMethod.POST})
	public ModelAndView commentPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		
		if(null!=requestMap && requestMap.size()>0){	
			try {
				Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
				Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
				CommentVO  commentVO  = CommentHelperManager.enrichCommentVO(requestMap,clientMap);
				commentVO.setCommenterId(userId);
				dataMap = commentService.createComment(commentVO);
				logger.debug("dataMap "+dataMap);
				if(null!=dataMap && dataMap.size()>0){
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.COMMENT_POST_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.COMMENT_POST_SUCCESS.getSuccessCode());
				}else{
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION.getErrorCode());
				}
			} catch (CommentServiceException e) {
				logger.error("ContentCreateController.createStationContent() "+e.getLocalizedMessage() ,e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}catch (Exception e) {
					logger.error("ContentCreateController.createStationContent() "+e.getLocalizedMessage() ,e);
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION.getErrorCode());
			}
		}else{
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.COMMENT_SERVICE_FAILED_EXCEPTION.getErrorCode());
		}

		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		
		return modelAndView;
	}

	@RequestMapping(value = "/vzpb/get", method = RequestMethod.GET)
	public ModelAndView getCommentList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		Long contentId=0l;
		
		String jsonData = null;
		int start = 0;
		int end = 10;
		try {
			Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			contentId = CommentHelperManager.enrichCommentVO(httpServletRequest);
			logger.debug("getCommentList.contentId="+contentId);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			dataMap = commentService.getCommentList(contentId, start, end,clientMap,userId);
			logger.debug("getCommentList.dataMap="+dataMap);
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_COMMENT_SERVICE_FAILED_EXCEPTION.getErrorMessage());
		}
		
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.CONTENT_SUCCESS.getSuccessCode());
				jsonData = gson.toJson(dataMap);
		}
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
		

	@RequestMapping(value = "/vzpb/get/more", method = RequestMethod.GET)
	public ModelAndView getMoreCommentList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String jsonData = null;
		try {
			int end =Integer.parseInt(httpServletRequest.getParameter(VoizdStationContentParam.ENDLIMIT));
			int start =Integer.parseInt(httpServletRequest.getParameter(VoizdStationContentParam.STARTLIMIT));
			Long contentId = Long.parseLong(httpServletRequest.getParameter(VoizdStationContentParam.CONTENT_ID));
			Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			dataMap = commentService.getCommentList(contentId, start, end,clientMap,userId);
			logger.debug("getMoreCommentList.dataMap="+dataMap);
		}catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.USER_COMMENT_SERVICE_FAILED_EXCEPTION.getErrorMessage());
		}
		if(null!=dataMap && dataMap.size()>0){
			jsonData = gson.toJson(dataMap);
		}else{
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
				dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.COMMENT_SUCCESS.getSuccessMessage());
				jsonData = gson.toJson(dataMap);
		}
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	
	@RequestMapping(value = "/vzpr/delete", method = RequestMethod.POST)
	public ModelAndView deleteComment(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
		if(null!=requestMap && requestMap.size()>0 ){
			
			try {
					Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
					CommentVO  commentVO  = CommentHelperManager.enrichUpdateCommentVO(requestMap);
					commentVO.setCommenterId(userId);
					commentService.deleteComment(commentVO.getCommentId(),commentVO.getCommenterId());
					dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
					dataMap.put(VoizdWebConstant.MESSAGE, SuccessCodesEnum.COMMENT_DELETE_SUCCESS.getSuccessMessage());
					dataMap.put(VoizdWebConstant.CODE, SuccessCodesEnum.COMMENT_DELETE_SUCCESS.getSuccessCode());

			}catch(CommentServiceException e){
				logger.error("ContentCreateController.deleteStationContent() "+e.getLocalizedMessage() ,e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				if(ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION.getErrorCode().equalsIgnoreCase(e.getErrorCode())){
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION.getErrorCode());
				}else if(ErrorCodesEnum.STATION_CONTENT_INVALID_USER.getErrorCode().equalsIgnoreCase(e.getErrorCode())){
					dataMap.put(VoizdWebConstant.MESSAGE,ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE,ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION.getErrorCode());
				}else{
					dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
					dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION.getErrorCode());
				}
			}catch(Exception e){
				logger.error("ContentCreateController.deleteStationContent() "+e.getLocalizedMessage() ,e);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
				dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
				dataMap.put(VoizdWebConstant.CODE, ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION.getErrorCode());
				
			}
		}else{
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION.getErrorMessage());
			dataMap.put(VoizdWebConstant.MESSAGE, ErrorCodesEnum.COMMENT_DELETE_FAILED_EXCEPTION.getErrorCode());
		}
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);	
		return modelAndView;
	}

}
