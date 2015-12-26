package com.voizd.modules.push.message.service;

import java.util.Map;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.modules.push.message.util.PushNotificationConstants;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;
import com.voizd.modules.agent.util.resource.AgentResourceUtils;

public class IphonePushMessageServiceImpl implements PushMessageService {
	private static Logger logger = LoggerFactory.getLogger(IphonePushMessageServiceImpl.class);
	private String apnHost;
	private int apnPort;
	private String certPath;
	private String password;
	PushNotificationManager pushManager = null;
	
	public IphonePushMessageServiceImpl(){
		try{
		pushManager = PushNotificationManager.getInstance();
		//apnHost = "gateway.push.apple.com";
		apnHost = AgentResourceUtils.getSystemConfigProperty("iphone.apnHost");
		apnPort = Integer.parseInt(AgentResourceUtils.getSystemConfigProperty("iphone.apnPort"));
		certPath = AgentResourceUtils.getSystemConfigProperty("iphone.certPath");
		password = AgentResourceUtils.getSystemConfigProperty("iphone.password");
		initializeConnection();
		}catch (Exception e) {
			logger.error("Exception in IphonePushMessageServiceImpl  "+e.getLocalizedMessage(),e);
		}
	}

	@Override
	public boolean pushNotification(Map<String, Object> params) {
		try{
			
		String deviceKey = (String) params.get(PushNotificationConstants.DEVICE_KEY);
		//String deviceKey ="1851660bb1c86c5da6dd9fe85e4ca223dd0969459727de7e394a22613576656e";
		//String deviceKey ="4ea808dd7fb7ae6f283c3f254ace8dd5891c44d62e7e073f1e73f8f54aa1e02b";
		//Long userId = (Long) params.get(PushNotificationConstants.USERID);
		String pushMessage = (String) params.get(PushNotificationConstants.PUSH_MESSAGE);
		
		
		logger.debug("PushNotification:: deviceKey :: "+deviceKey);
		logger.debug("PushNotification:: pushMessage :: "+pushMessage);
		
		logger.debug("PushNotification:: pushMessage pushManager :: "+pushManager);
		
		pushManager.addDevice("iPhone", deviceKey);
		logger.debug("PushNotification:: adding :: ");
		Device client = pushManager.getDevice("iPhone");
		logger.debug("PushNotification:: client one :: "+client);
		PayLoad payload = new PayLoad();
		payload.addBadge(1);
		payload.addAlert(pushMessage);
		payload.addSound("default");
		logger.debug("PushNotification:: pushMessage two :: "+pushMessage);
		pushManager.sendNotification(client, payload);
		logger.debug("PushNotification:: pushMessage :: end "+pushMessage);
		}catch (Exception e) {
			logger.error(" occurred while trying to push notification to iPhone!!. Reason: " + e.getLocalizedMessage(), e);
			return false;
		} finally {
			try {
				if (pushManager != null) {
					pushManager.removeDevice("iPhone");
				}
			} catch (Exception e) {
				logger.error(e.getClass().getName()+ " occurred while removing device!!. Reason: " + e.getLocalizedMessage(), e);
			}
		}
		return false;
	}
	
	private void initializeConnection() throws KeyManagementException,
			UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, NoSuchProviderException, IOException {
		pushManager.initializeConnection(apnHost, apnPort, certPath, password,
				SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
		logger.info("Connection with APNs established successfully...");
	}

}
