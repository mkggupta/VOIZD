package com.voizd.service.comment.helper;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.CommentVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.entities.CommentInfo;

public class CommentUtils {
	private static Logger logger = LoggerFactory.getLogger(CommentUtils.class);
	public static CommentInfo transformCommentInfo(CommentVO commentVO,UserVO userVO, Long ownerId){
		CommentInfo commentInfo = new CommentInfo();
		try {
			BeanUtils.copyProperties(commentInfo, commentVO);
			commentInfo.setCommenterUserId(commentVO.getCommenterId());
			commentInfo.setCommenterName(userVO.getUserName());
			commentInfo.setOwnerUserId(ownerId);
			commentInfo.setStatus(VoizdConstant.COMMENT_ACTIVE_STATUS);
		} catch (IllegalAccessException e) {
			logger.error("Error in copying commentVO " + commentVO + " to commentInfo", e);
		} catch (InvocationTargetException e) {
			logger.error("Error in copying commentVO " + commentVO + " to commentInfo", e);
		}
		
		return commentInfo;
		
	}

}
