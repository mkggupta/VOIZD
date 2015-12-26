package com.voizd.rest.controller.search;

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

import com.voizd.common.beans.vo.SearchVO;
import com.voizd.common.util.ClientHeaderUtil;
import com.voizd.common.util.ControllerUtils;
import com.voizd.rest.constant.VoizdWebConstant;
import com.voizd.rest.helper.manager.SearchHelperManager;
import com.voizd.service.search.exception.SearchServiceException;
import com.voizd.service.search.v1_0.SearchService;



@Controller
@RequestMapping("/api/search/vzpb")
public class SearchController {
	private static Logger logger = LoggerFactory.getLogger(SearchController.class);
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	private SearchService searchService;
	
	@RequestMapping(value = "/station", method = RequestMethod.POST)
	public ModelAndView getSearchStation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			int startLimit = 0;
			int pageLimit = 10;

			if (requestMap != null && requestMap.size() > 0) {
				Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
				SearchVO searchVO = SearchHelperManager.enrichSearchVO(requestMap, userId);
				logger.debug("getSearchStation :: tag :: " + searchVO.getTag() + ", search Type :: " + searchVO.getSearchType());

				Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
				dataMap = searchService.getStationSearch(searchVO, startLimit, pageLimit, clientMap);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);
			} else {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			}
		} catch (SearchServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			logger.error("getSearchStation() :: Error while saerch " + e.getLocalizedMessage(), e);
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			logger.error("Exception while saerch " + e.getLocalizedMessage(), e);
		}

		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		logger.debug("getSearchStation :: jsonData :: " + jsonData);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/content", method = RequestMethod.POST)
	public ModelAndView getSearchContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		try {
			HashMap<String, String> requestMap = ControllerUtils.getRequestMapFromMultipart(httpServletRequest);
			int startLimit = 0;
			int endLimit =VoizdWebConstant.DEFAULT_SEARCH_LIMIT;

			if (requestMap != null && requestMap.size() > 0) {
				Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
				SearchVO searchVO = SearchHelperManager.enrichSearchVO(requestMap, userId);
				logger.debug("getSeacrhContent :: tag :: " + searchVO.getTag() + " , searchVO : " + searchVO.getSearchType());

				Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
				dataMap = searchService.getContentSearch(searchVO, startLimit, endLimit, clientMap);
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_SUCCESS);

			} else {
				dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			}
		} catch (SearchServiceException e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			logger.error("getSeacrhContent() :: Error while saerch " + e.getLocalizedMessage(), e);
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			logger.error("Exception while saerch " + e.getLocalizedMessage(), e);
		}
		
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		logger.debug("getSeacrhContent :: jsonData :: " + jsonData);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/station/more", method = { RequestMethod.GET })
	public ModelAndView getSearchMoreStation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		try {
			Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			SearchVO searchVO = SearchHelperManager.enrichSearchVO(httpServletRequest,userId);
			int startLimit = searchVO.getStartLimit();
			int pageLimit = searchVO.getPageLimit();
			logger.debug("getSearchMoreStation :: tag :: " + searchVO.getTag() + ", startLimit :: " + startLimit + " , pageLimit :: " + pageLimit);

			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			dataMap = searchService.getStationSearch(searchVO, startLimit, pageLimit, clientMap);
		} catch (SearchServiceException e) {
			logger.error("getSeacrhMoreStation() :: Error while saerch " + e.getLocalizedMessage(), e);
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			logger.error("Exception while saerch " + e.getLocalizedMessage(), e);
		}
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		logger.debug("getSeacrhMoreStation :: jsonData :: " + jsonData);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}

	@RequestMapping(value = "/content/more", method = { RequestMethod.GET })
	public ModelAndView getSearchMoreContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView modelAndView = new ModelAndView();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		try {
			Long userId = ControllerUtils.extractUserIdFromHeader(httpServletRequest);
			SearchVO searchVO = SearchHelperManager.enrichSearchVO(httpServletRequest,userId);
			int startLimit = searchVO.getStartLimit();
			int endLimit = searchVO.getPageLimit();
			logger.debug("getSearchMoreContent :: tag :: " + searchVO.getTag());

			Map<String, Object> clientMap = ClientHeaderUtil.extractClientParam(httpServletRequest);
			dataMap = searchService.getContentSearch(searchVO, startLimit, endLimit, clientMap);
		} catch (SearchServiceException e) {
			logger.error("getSeacrhMoreContent() :: Error while saerch " + e.getLocalizedMessage(), e);
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
		} catch (Exception e) {
			dataMap.put(VoizdWebConstant.STATUS, VoizdWebConstant.STATUS_ERROR);
			logger.error("Exception while saerch " + e.getLocalizedMessage(), e);
		}
		Gson gson = new Gson();
		String jsonData = gson.toJson(dataMap);
		logger.debug("getSeacrhMoreContent :: jsonData :: " + jsonData);
		modelAndView.setViewName(VoizdWebConstant.DEFAULT_VIEW_NAME);
		modelAndView.addObject(VoizdWebConstant.RESPONSE, jsonData);
		return modelAndView;
	}

}
