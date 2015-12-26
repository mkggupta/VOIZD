package com.voizd.service.tagcloud.bo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;
import java.util.regex.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.entities.EarthInfo;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.earth.EarthDAO;
import com.voizd.service.tagcloud.exception.TagCloudServiceException;
import com.voizd.service.tagcloud.helper.TagCloudUtils;
import com.voizd.tagcloud.Cloud;
import com.voizd.tagcloud.Tag;
import com.voizd.tagcloud.filters.DictionaryFilter;
import com.voizd.tagcloud.filters.Filter;

public class TagCloudBOImpl implements TagCloudBO {
	Logger logger = LoggerFactory.getLogger(TagCloudBOImpl.class);
	private static final String HASHTAG_CHARACTERS = "[a-z0-9_\\u00c0-\\u00d6\\u00d8-\\u00f6\\u00f8-\\u00ff]";
	public static final Pattern pattern = Pattern.compile("(^|[^0-9A-Z&/]+)(#|\uFF03)([0-9A-Z_]*[A-Z_]+" + HASHTAG_CHARACTERS + "*)", Pattern.CASE_INSENSITIVE);
	
	private EarthDAO earthDAO;
	public void setEarthDAO(EarthDAO earthDAO) {
		this.earthDAO = earthDAO;
	}
	@Override
	public void createTagCloud(ContentVO contentVO) throws TagCloudServiceException {
		Cloud cloud = new Cloud(); 
		cloud.setMaxTagsToDisplay(20);
		Filter<Tag> filter = new DictionaryFilter();
		Set<Filter<Tag>> outputFilters = new HashSet<Filter<Tag>>();
		outputFilters.add(filter);
		cloud.addText(contentVO.getTitle());
		cloud.setOutputFilters(outputFilters);
		cloud.tags();
		EarthInfo earthInfo = TagCloudUtils.transformContentVOToEarthInfo(contentVO);
		//earthInfo.setFileIds(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(VoizdConstant.AUDIO,MediaUtil.getMediaContentUrl(contentVO.getFileIds(),VoizdConstant.MP3,null),true)));
		for (Tag tag : cloud.tags()) {
			earthInfo.setTagName(tag.getName());
			try {
				if(StringUtils.isBlank(earthInfo.getState())){
					earthInfo.setState(VoizdConstant.DEFAULT_STATE);
				}
				if(StringUtils.isBlank(earthInfo.getCity())){
					earthInfo.setCity(VoizdConstant.DEFAULT_CITY);
				}
				earthDAO.saveOrUpdateEarthTag(earthInfo);
			} catch (DataUpdateFailedException e) {
				logger.error("Exception in getcreatTagCloud  for contentVO="+contentVO+" , tag="+tag.getName(), e.getLocalizedMessage());
			}
		}
		
	}
	
	@Override
	public String createTagCloud (ContentVO contentVO, int status) throws TagCloudServiceException{
		String tags =null;
		
		List<String> titles = getHashTag(contentVO.getTitle());
		logger.debug("titles ::::::::::::::: "+titles);
		String title = getTitle(titles,contentVO.getTitle());
		logger.debug("title ::::::::::::::: "+title);
		//contentVO.setTitle(title);
		logger.debug("contentVO.getTitle() ::::::::::::::::---"+contentVO.getTitle());
		StringBuilder tagBuilder = new StringBuilder();
		Cloud cloud = new Cloud(); 
		cloud.setMaxTagsToDisplay(20);
		Filter<Tag> filter = new DictionaryFilter();
		Set<Filter<Tag>> outputFilters = new HashSet<Filter<Tag>>();
		outputFilters.add(filter);
		cloud.addText(title.toUpperCase());
		logger.debug("createTagCloud---"+contentVO.getTag());
		if(StringUtils.isNotBlank(contentVO.getTag())){
			cloud.addText(contentVO.getTag());
		 }
		cloud.setOutputFilters(outputFilters);
		cloud.tags();
		EarthInfo earthInfo = TagCloudUtils.transformContentVOToEarthInfo(contentVO);
		//earthInfo.setFileIds(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(VoizdConstant.AUDIO,MediaUtil.getMediaContentUrl(contentVO.getFileIds(),VoizdConstant.MP3,null),true)));
		for (Tag tag : cloud.tags()) {
			earthInfo.setTagName(tag.getName());
			tagBuilder.append(tag.getName()).append(VoizdConstant.TAG_SEPERATOR);
			try {
				if(StringUtils.isBlank(earthInfo.getState())){
					earthInfo.setState(VoizdConstant.DEFAULT_STATE);
				}
				if(StringUtils.isBlank(earthInfo.getCity())){
					earthInfo.setCity(VoizdConstant.DEFAULT_CITY);
				}
				if(VoizdConstant.ACTIVE_USER_STATUS==status){
					earthDAO.saveOrUpdateEarthTag(earthInfo);
				}
			} catch (DataUpdateFailedException e) {
				logger.error("Exception in getcreatTagCloud  for contentVO="+contentVO+" , tag="+tag.getName(), e.getLocalizedMessage());
			}
		}
		for(String hashtag : titles){
			tagBuilder.append("#").append(hashtag).append(VoizdConstant.TAG_SEPERATOR);
		}
		if(tagBuilder.length()>0){
			tags = tagBuilder.substring(0, tagBuilder.length()-1).toString();
		}
		
		/*if(StringUtils.isBlank(tags)){
			tags = contentVO.getTitle();
		}*/
		
		return tags;
	}
	
	private List<String> getHashTag(String title){
		  List<String> extracted = new ArrayList<String>();
		    Matcher matcher = pattern.matcher(title);
		    while (matcher.find()) {
		      extracted.add(matcher.group(3));
		    }
		    return extracted;
	}
	
	private String getTitle(List<String> extracted,String title){
		    String text = title;
		 for(String tag :extracted){
			 text = text.replaceAll("#"+tag, "");
		    }
		    return text;
	}
}
