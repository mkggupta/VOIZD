/**
 * 
 */
package com.voizd.service.user.helper;

import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.common.util.CDNUrlUtil;
import com.voizd.common.util.MediaUtil;
import com.voizd.common.util.VoizdUrlUtils;

/**
 * @author Manish
 * 
 */
public class UserManagerHelper {

	public static void enrichUser(UserVO userVO, String imageResolution) {
		if (null != userVO) {
			if (null != userVO.getProfilePicFileId() && null != userVO.getProfilePicFileExt()) {
				userVO.setProfilePicUrl(VoizdUrlUtils.encodedUrl(CDNUrlUtil.getCdnContentUrl(VoizdConstant.IMAGE,
						MediaUtil.getMediaContentUrl(userVO.getProfilePicFileId(), userVO.getProfilePicFileExt(), imageResolution), false)));
			}
		}
	}

}
