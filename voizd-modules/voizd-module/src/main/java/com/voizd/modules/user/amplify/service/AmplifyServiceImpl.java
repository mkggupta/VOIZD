package com.voizd.modules.user.amplify.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.AmplifyInfoVO;
import com.voizd.common.beans.vo.StationFollowerVO;
import com.voizd.common.beans.vo.StationVO;
import com.voizd.common.beans.vo.TapClipVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.modules.station.StationDAO;
import com.voizd.dao.modules.station.StationSubDAO;
import com.voizd.dao.mongo.service.MongoServiceImpl;
import com.voizd.modules.user.amplify.util.AmplifyUtil;

public class AmplifyServiceImpl {
	private static Logger logger = LoggerFactory.getLogger(AmplifyServiceImpl.class);
	private StationSubDAO stationSubDAO;	
	private StationDAO stationDAO;
	public void setStationDAO(StationDAO stationDAO) {
		this.stationDAO = stationDAO;
	}


	public void setStationSubDAO(StationSubDAO stationSubDAO) {
		this.stationSubDAO = stationSubDAO;
	}

	
	public void doTask(AmplifyInfoVO amplifyInfoVO,String command) throws Exception {
		logger.debug("JMS  doTask ::amplifyInfoVO=" + amplifyInfoVO + " command=" + command);
		
		if(null!=amplifyInfoVO){
		 TapClipVO tapClipVO = AmplifyUtil.transformTapClipVO(amplifyInfoVO);
			MongoServiceImpl mongoServiceImpl = MongoServiceImpl.getInstance();
			if (VoizdConstant.AMPLIFY_COMMAND.equalsIgnoreCase(command)) {
				StationVO stationVO = stationDAO.getUserStationInfo(tapClipVO.getAmplifierId(),VoizdConstant.ACTIVE_STATION_STATUS);
				if(null!=stationVO){
					tapClipVO.setStationId(stationVO.getStationId());
					logger.debug("JMS  setStationId ::" + tapClipVO.getStationId());
					List<StationFollowerVO> stationFollowerList = stationSubDAO.getStationFollowerList(tapClipVO.getStationId(), tapClipVO.getAmplifierId(), 0, 100);
					for (StationFollowerVO stationFollowerVO : stationFollowerList) {
						logger.debug("JMS  CreatorId ::getFollowerId " + stationFollowerVO.getFollowerId());
						mongoServiceImpl.upsertAmplifyVStream(tapClipVO,stationFollowerVO.getFollowerId());
					}
				}
			
			    tapClipVO.setAmplify(true);
				mongoServiceImpl.upsertAmplifyVStream(tapClipVO,tapClipVO.getAmplifierId());
				mongoServiceImpl.upsertAmplifyMyStreamClip(tapClipVO);
			}else if (VoizdConstant.DEAMPLIFY_COMMAND.equalsIgnoreCase(command)) {
				StationVO stationVO = stationDAO.getUserStationInfo(tapClipVO.getAmplifierId(),VoizdConstant.ACTIVE_STATION_STATUS);
				logger.debug("JMS  setStationId ::" + tapClipVO.getStationId()+" , command :: "+command);
				/*if(null!=stationVO){
					tapClipVO.setStationId(stationVO.getStationId());
					logger.debug("JMS  setStationId ::" + tapClipVO.getStationId());
					List<StationFollowerVO> stationFollowerList = stationSubDAO.getStationFollowerList(tapClipVO.getStationId(), tapClipVO.getAmplifierId(), 0, 100);
					for (StationFollowerVO stationFollowerVO : stationFollowerList) {
						mongoServiceImpl.upsertAmplifyVStream(tapClipVO,stationFollowerVO.getFollowerId());
						logger.debug("JMS  CreatorId ::" + tapClipVO.getCreatorId());
					}
				}
				mongoServiceImpl.upsertAmplifyVStream(tapClipVO,tapClipVO.getAmplifierId());
				mongoServiceImpl.upsertMyStreamClip(tapClipVO);*/
			}
		}else{
			logger.debug("JMS  else ::amplifyInfoVO=" + amplifyInfoVO + " command=" + command);
		}
	}
}
