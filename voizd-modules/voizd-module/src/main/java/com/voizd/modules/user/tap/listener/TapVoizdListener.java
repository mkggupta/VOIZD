package com.voizd.modules.user.tap.listener;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.TapClipVO;
import com.voizd.dao.modules.amplify.AmplifyDAO;
import com.voizd.dao.modules.content.ContentCounterDAO;
import com.voizd.dao.modules.station.StationSubDAO;
import com.voizd.dao.modules.stationcounter.StationCounterDAO;
import com.voizd.dao.modules.user.UserCounterDAO;
import com.voizd.modules.user.tap.service.TapServiceImpl;

public class TapVoizdListener implements MessageListener {
	private static Logger logger = LoggerFactory.getLogger(TapVoizdListener.class);
	public void setStationSubDAO(StationSubDAO stationSubDAO) {
		this.stationSubDAO = stationSubDAO;
	}

	public void setStationCounterDAO(StationCounterDAO stationCounterDAO) {
		this.stationCounterDAO = stationCounterDAO;
	}
	public void setContentCounterDAO(ContentCounterDAO contentCounterDAO) {
		this.contentCounterDAO = contentCounterDAO;
	}

	public void setAmplifyDAO(AmplifyDAO amplifyDAO) {
		this.amplifyDAO = amplifyDAO;
	}
	public void setUserCounterDAO(UserCounterDAO userCounterDAO) {
		this.userCounterDAO = userCounterDAO;
	}

	private StationSubDAO stationSubDAO;
	private StationCounterDAO stationCounterDAO;
	private ContentCounterDAO contentCounterDAO ;
	private AmplifyDAO amplifyDAO ;
	private UserCounterDAO  userCounterDAO;
	
	public TapVoizdListener() {
	}
	
	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				logger.debug("JMS  messageobj::");
				Object msgObj = ((ObjectMessage)message).getObject();
				if (msgObj instanceof HashMap) {
					logger.debug("JMS  HashMap obj::");
					Map<String, Object> msgMap = (Map<String, Object>) msgObj;
					TapClipVO tapClipVO = (TapClipVO)msgMap.get("content");
					String command = (String)msgMap.get("command");
					logger.debug("JMS  messageobj:: command ::"+command);
					TapServiceImpl tapServiceImpl = new TapServiceImpl();
					tapServiceImpl.setStationCounterDAO(stationCounterDAO);
					tapServiceImpl.setStationSubDAO(stationSubDAO);
					tapServiceImpl.setAmplifyDAO(amplifyDAO);
					tapServiceImpl.setUserCounterDAO(userCounterDAO);
					tapServiceImpl.setContentCounterDAO(contentCounterDAO);
					tapServiceImpl.doTask(tapClipVO,command);
				}
			
			}else{
				logger.debug("JMS text message other obj::");
			}
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("Exception in onMessage :: "+e.getLocalizedMessage(), e);
			}
		}
}
