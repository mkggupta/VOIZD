package com.voizd.service.tagcloud.helper;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.ContentVO;
import com.voizd.dao.entities.EarthInfo;

public class TagCloudUtils {
	static Logger logger = LoggerFactory.getLogger(TagCloudUtils.class);
	public static EarthInfo transformContentVOToEarthInfo(ContentVO contentVO) {
		EarthInfo earthInfo = new EarthInfo();
		if (null != contentVO) {
			try {
				BeanUtils.copyProperties(earthInfo, contentVO);
			} catch (IllegalAccessException e) {
				logger.error("Error in copying contentVO " + contentVO + " to earthInfo", e);
			} catch (InvocationTargetException e) {
				logger.error("Error in copying contentVO " + contentVO + " to earthInfo", e);
			}
		}
		return earthInfo;
	}
}
