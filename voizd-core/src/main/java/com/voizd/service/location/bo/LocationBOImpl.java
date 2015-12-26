package com.voizd.service.location.bo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.IpToLocationVO;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.geoip.Location;
import com.voizd.geoip.LookupService;
import com.voizd.geoip.regionName;
import com.voizd.service.location.exception.LocationServiceException;

public class LocationBOImpl implements LocationBO {
	Logger logger = LoggerFactory.getLogger(LocationBOImpl.class);
	
	@Override
	public IpToLocationVO getIpToLocation(String fileLocation,String mobileIp) throws LocationServiceException{
		IpToLocationVO ipToLocationVO = new IpToLocationVO();
		LookupService cl = null;
		try {
			cl = new LookupService(fileLocation,
					LookupService.GEOIP_MEMORY_CACHE);
			Location l2 = cl.getLocation(mobileIp);
			if(null!=l2){
				ipToLocationVO.setCountry(l2.countryName);
				ipToLocationVO.setState(regionName.regionNameByCode(l2.countryCode, l2.region));
				ipToLocationVO.setCity(l2.city);
				ipToLocationVO.setLatitude(l2.latitude);
				ipToLocationVO.setLongitude(l2.longitude);
				logger.debug("countryCode="+l2.countryCode);
				logger.debug("countryName="+l2.countryName);
				logger.debug("state="+regionName.regionNameByCode(l2.countryCode, l2.region));
				logger.debug("ipToLocationVO="+ipToLocationVO);
			
			}else{
				logger.debug("could not get any info");
			}
		} catch (IOException e1) {
			throw new LocationServiceException(ErrorCodesEnum.INVALID_REQUEST_EXCEPTION);
		}catch (Exception e) {	
			e.printStackTrace();
		}finally{
			cl.close();	
		}
		return ipToLocationVO;
	}
}
