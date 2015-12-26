package com.voizd.modules.user.amplify.util;

import com.voizd.common.beans.vo.AmplifyInfoVO;
import com.voizd.common.beans.vo.TapClipVO;

public class AmplifyUtil {
	
	public static TapClipVO transformTapClipVO(AmplifyInfoVO amplifyInfoVO){
		TapClipVO tapClipVO = new TapClipVO();
		tapClipVO.setContentId(amplifyInfoVO.getContentId());
		tapClipVO.setCreatorId(amplifyInfoVO.getCreatorId());
		tapClipVO.setCreatedDate(amplifyInfoVO.getCreatedDate());
		tapClipVO.setAmplifierId(amplifyInfoVO.getAmplifierId());
		return tapClipVO;
		
	}

}
