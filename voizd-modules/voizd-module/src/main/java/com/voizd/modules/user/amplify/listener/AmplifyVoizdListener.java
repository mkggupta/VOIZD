package com.voizd.modules.user.amplify.listener;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.AmplifyInfoVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.modules.station.StationDAO;
import com.voizd.dao.modules.station.StationSubDAO;
import com.voizd.modules.user.amplify.service.AmplifyServiceImpl;

public class AmplifyVoizdListener implements MessageListener {
	private static Logger logger = LoggerFactory.getLogger(AmplifyVoizdListener.class);
	private StationSubDAO stationSubDAO;
	private StationDAO stationDAO;
	public void setStationSubDAO(StationSubDAO stationSubDAO) {
		this.stationSubDAO = stationSubDAO;
	}
	public void setStationDAO(StationDAO stationDAO) {
		this.stationDAO = stationDAO;
	}

	public AmplifyVoizdListener() {
	}
	
	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				logger.debug("JMS  messageobj AmplifyVoizdListener::"+message);
				Object msgObj = ((ObjectMessage)message).getObject();
				if (msgObj instanceof HashMap) {
					logger.debug("JMS  HashMap obj::");
					Map<String, Object> msgMap = (Map<String, Object>) msgObj;
					AmplifyInfoVO amplifyInfoVO = (AmplifyInfoVO)msgMap.get(VoizdConstant.AMPLIFY_KEY);
					String command = (String)msgMap.get(VoizdConstant.COMMAND_KEY);
					logger.debug("JMS  messageobj:: command ::"+command);
					AmplifyServiceImpl amplifyService = new AmplifyServiceImpl();
					amplifyService.setStationSubDAO(stationSubDAO);
					amplifyService.setStationDAO(stationDAO);
					amplifyService.doTask(amplifyInfoVO,command);
				}
			
			}else{
				logger.debug("JMS text message other obj::");
			}
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error("Exception in AmplifyVoizdListener.onMessage :: "+e.getLocalizedMessage(), e);
			}
		}
}
