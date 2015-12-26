/**
 * 
 */
package com.voizd.service.user.helper;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.dto.UserDTO;
import com.voizd.common.beans.vo.AuthenticationDetailsVO;
import com.voizd.common.beans.vo.RegistrationVO;
import com.voizd.common.beans.vo.UserVO;
import com.voizd.common.enumeration.UserStatusEnum;
import com.voizd.dao.entities.UserAuth;
import com.voizd.dao.entities.UserInfo;

/**
 * @author Manish
 * 
 */
public class UserUtils {
	static Logger logger = LoggerFactory.getLogger(UserUtils.class);

	public static UserVO transformUserInfoToUserVO(UserInfo userInfo) {
		UserVO userVO = new UserVO();
		if (null != userInfo) {
			try {
				if(userInfo.getCity() == null){
					userInfo.setCity("");
				}
				if(userInfo.getWebSite() == null){
					userInfo.setWebSite("");
				}
				BeanUtils.copyProperties(userVO, userInfo);
			} catch (IllegalAccessException e) {
				logger.error("Error in copying userInfo " + userInfo + " to userVO", e);
			} catch (InvocationTargetException e) {
				logger.error("Error in copying userInfo " + userInfo + " to userVO", e);
			}
		}
		return userVO;
	}

	public static UserInfo transformUserVOToUserInfo(UserVO userVO) {
		UserInfo userInfo = new UserInfo();
		if (null != userVO) {
			try {
				BeanUtils.copyProperties(userInfo, userVO);
			} catch (IllegalAccessException e) {
				logger.error("Error in copying userVO " + userVO + " to userInfo", e);
			} catch (InvocationTargetException e) {
				logger.error("Error in copying userVO " + userVO + " to userInfo", e);
			}
			userInfo.setModifiedDate(new Date());

		}
		return userInfo;
	}

	public static UserAuth transformRegisterationVOToUserAuth(RegistrationVO registrationVO, boolean isRegistration) {
		UserAuth userAuth = new UserAuth();
		if (null != registrationVO) {
			userAuth.setId(registrationVO.getId());
			userAuth.setPassword(registrationVO.getPassword());
			userAuth.setRegistrationMode(registrationVO.getRegistrationMode());
			userAuth.setUserName(registrationVO.getUserName());
			userAuth.setCurrentClientVersion(registrationVO.getCurrentClientVersion());
			userAuth.setCurrentPlatform(registrationVO.getCurrentPlatform());
			if (isRegistration) {
				if (registrationVO.getRegistrationMode() > 0) {
					userAuth.setStatus(UserStatusEnum.ACTIVE.getStatus());
				} else {
					userAuth.setStatus(UserStatusEnum.INACTIVE.getStatus());
				}
				userAuth.setLastLoginMode(registrationVO.getRegistrationMode());
				userAuth.setCreatedDate(new Date());
				userAuth.setLoginStatus(0);
				userAuth.setLatitude(registrationVO.getLatitude());
				userAuth.setLongitude(registrationVO.getLongitude());
				userAuth.setLastLocation(registrationVO.getLastLocation());
			}
			userAuth.setLastLoginTime(new Date());
			userAuth.setModifiedDate(new Date());
			userAuth.setPushKey(registrationVO.getPushKey());
		}
		return userAuth;
	}

	public static void updateDBUserVO(UserVO dbUserVO, UserVO userVO) {
		if (null != userVO && null != dbUserVO) {
			if (null != userVO.getFirstName()) {
				dbUserVO.setFirstName(userVO.getFirstName());
			}
			if (null != userVO.getLastName()) {
				dbUserVO.setLastName(userVO.getLastName());
			}
			if (null != userVO.getCity()) {
				dbUserVO.setCity(userVO.getCity());
			}
			if (null != userVO.getContactAddressLine1()) {
				dbUserVO.setContactAddressLine1(userVO.getContactAddressLine1());
			}
			if (null != userVO.getContactAddressLine2()) {
				dbUserVO.setContactAddressLine2(userVO.getContactAddressLine2());
			}
			if (null != userVO.getContactNumber()) {
				dbUserVO.setContactNumber(userVO.getContactNumber());
			}
			if (null != userVO.getCountry()) {
				dbUserVO.setCountry(userVO.getCountry());
			}
			if (null != userVO.getProfilePicFileId()) {
				dbUserVO.setProfilePicFileId(userVO.getProfilePicFileId());
			}
			if (null != userVO.getProfilePicFileExt()) {
				dbUserVO.setProfilePicFileExt(userVO.getProfilePicFileExt());
			}
			if (null != userVO.getSalutation()) {
				dbUserVO.setSalutation(userVO.getSalutation());
			}
			if (null != userVO.getSecondaryEmailAddress()) {
				dbUserVO.setSecondaryEmailAddress(userVO.getSecondaryEmailAddress());
			}
			if (null != userVO.getState()) {
				dbUserVO.setState(userVO.getState());
			}
			if (null != userVO.getTimeZone()) {
				dbUserVO.setTimeZone(userVO.getTimeZone());
			}
			if (null != userVO.getZipcode()) {
				dbUserVO.setZipcode(userVO.getZipcode());
			}
			if (null != userVO.getLanguage()) {
				dbUserVO.setLanguage(userVO.getLanguage());
			}
			if (null != userVO.getLocation()) {
				dbUserVO.setLocation(userVO.getLocation());
			}
			if (null != userVO.getUserDescription()) {
				dbUserVO.setUserDescription(userVO.getUserDescription());
			}
			if (null != userVO.getWebSite()) {
				dbUserVO.setWebSite(userVO.getWebSite());
			}
		}
	}

	public static AuthenticationDetailsVO transformUserAuthToAuthenticationDetailsVO(UserAuth userAuth) {
		AuthenticationDetailsVO authenticationDetailsVO = new AuthenticationDetailsVO();
		if (null != userAuth) {
			try {
				BeanUtils.copyProperties(authenticationDetailsVO, userAuth);
			} catch (IllegalAccessException e) {
				logger.error("Error in copying userAuth " + userAuth + " to AuthenticationDetailsVO", e);
			} catch (InvocationTargetException e) {
				logger.error("Error in copying userAuth " + userAuth + " to AuthenticationDetailsVO", e);
			}
		}
		return authenticationDetailsVO;
	}

	public static String displayName(String firstName, String lastName) {
		String displayName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
		return displayName.trim();

	}
	
	public static UserDTO transformUserVOToUserDTO(UserVO userVO) {
		UserDTO userDTO = new UserDTO();
		if (null != userVO) {
			try {
				BeanUtils.copyProperties(userDTO, userVO);
			} catch (IllegalAccessException e) {
				logger.error("Error in copying userVO " + userVO + " to userDto", e);
			} catch (InvocationTargetException e) {
				logger.error("Error in copying userVO " + userVO + " to userDto", e);
			}
		}
		return userDTO;
	}

}
