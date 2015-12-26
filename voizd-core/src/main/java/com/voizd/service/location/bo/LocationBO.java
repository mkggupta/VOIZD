package com.voizd.service.location.bo;

import com.voizd.common.beans.vo.IpToLocationVO;
import com.voizd.service.location.exception.LocationServiceException;


public interface LocationBO {

	IpToLocationVO getIpToLocation(String fileLocation,String mobileIp) throws LocationServiceException;

}
