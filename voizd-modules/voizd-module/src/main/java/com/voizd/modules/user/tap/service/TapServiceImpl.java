package com.voizd.modules.user.tap.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.AmplifyInfoVO;
import com.voizd.common.beans.vo.ContentCounterVO;
import com.voizd.common.beans.vo.StationCounterVO;
import com.voizd.common.beans.vo.StationFollowerVO;
import com.voizd.common.beans.vo.TapClipVO;
import com.voizd.common.beans.vo.UserCounterVO;
import com.voizd.common.constant.VoizdConstant;
import com.voizd.dao.modules.amplify.AmplifyDAO;
import com.voizd.dao.modules.content.ContentCounterDAO;
import com.voizd.dao.modules.station.StationSubDAO;
import com.voizd.dao.modules.stationcounter.StationCounterDAO;
import com.voizd.dao.modules.user.UserCounterDAO;
import com.voizd.dao.mongo.service.MongoServiceImpl;

public class TapServiceImpl implements TapService {
	private static Logger logger = LoggerFactory.getLogger(TapServiceImpl.class);

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
	



	@Override
	public boolean init() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doTask(TapClipVO tapClipVO, String command) throws Exception {
		logger.debug("JMS  doTask ::");
		MongoServiceImpl mongoServiceImpl = MongoServiceImpl.getInstance();
		if ("tap".equalsIgnoreCase(command)) {

			StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(tapClipVO.getStationId());
			mongoServiceImpl.insertVStream(tapClipVO, tapClipVO.getCreatorId());
			logger.debug("JMS  CreatorId ::" + tapClipVO.getCreatorId());
			if (stationCounterVO != null && stationCounterVO.getFollower() > 0) {
				int startLimit = 0;
				int endLimit = 50;
				try {
					endLimit = stationCounterVO.getFollower().intValue();
					logger.debug("JMS  CreatorId ::" + tapClipVO.getCreatorId() + ", stationid :: " + tapClipVO.getStationId());
					List<StationFollowerVO> stationFollowerList = stationSubDAO.getStationFollowerList(tapClipVO.getStationId(), tapClipVO.getCreatorId(),
							startLimit, endLimit);
					for (StationFollowerVO stationFollowerVO : stationFollowerList) {
						mongoServiceImpl.updateFollowerVStream(tapClipVO, stationFollowerVO.getFollowerId());
						mongoServiceImpl.upsertIFollowerVStream(tapClipVO, stationFollowerVO.getFollowerId());
					}
				} catch (Exception e) {
					logger.error("JMS  doTask tap command Exception :: " + e.getLocalizedMessage(), e);
				}
			}
		} else if ("uptap".equalsIgnoreCase(command)) {
			logger.debug("doTask() :: uptap :: stationId=" + tapClipVO.getStationId() + " , follwerId=" + tapClipVO.getFollweeId() + " , creatorId="
					+ tapClipVO.getCreatorId()+" ,Follwee id :: "+tapClipVO.getFollweeId());
                   //mongoServiceImpl.deleteFromIUserVStream(tapClipVO,tapClipVO.getFollweeId());
		} else if ("delete".equalsIgnoreCase(command)) {
			mongoServiceImpl.deleteContent(tapClipVO);
			//remove content from search
			StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(tapClipVO.getStationId());
			if (stationCounterVO != null && stationCounterVO.getFollower() > 0) {

				try {
					int startLimit = 0;
					int endLimit = stationCounterVO.getFollower().intValue();
					logger.debug("JMS  CreatorId ::" + tapClipVO.getCreatorId() + ", stationid :: " + tapClipVO.getStationId());
					mongoServiceImpl.deleteContentFromVStream(tapClipVO, tapClipVO.getCreatorId());
					mongoServiceImpl.deleteFromMyStream(tapClipVO, tapClipVO.getCreatorId());

					List<StationFollowerVO> stationFollowerList = stationSubDAO.getStationFollowerList(tapClipVO.getStationId(), tapClipVO.getCreatorId(),
							startLimit, endLimit);
					for (StationFollowerVO stationFollowerVO : stationFollowerList) {
						//mongoServiceImpl.deleteContentFromIFollow(tapClipVO, stationFollowerVO.getFollowerId());
						mongoServiceImpl.deleteContentFromVStream(tapClipVO, stationFollowerVO.getFollowerId());
						mongoServiceImpl.deleteFromMyStream(tapClipVO, stationFollowerVO.getFollowerId());
					}
				} catch (Exception e) {
					logger.error("JMS  doTask delete command Exception :: " + e.getLocalizedMessage(), e);
				}
			}
			
			ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(tapClipVO.getContentId());
			if(null!=contentCounterVO && contentCounterVO.getAmplify()>0){
				List<AmplifyInfoVO> amplifierCounterVOList = amplifyDAO.getAmplifierList(tapClipVO.getContentId(),0,contentCounterVO.getAmplify().intValue());
				for(AmplifyInfoVO amplifyInfoVO:amplifierCounterVOList){
					//delete clip
					
					amplifyInfoVO.setStatus(VoizdConstant.INACTIVE_AMPLIFY_STATUS);
					amplifyDAO.updateAmplifyInfo(amplifyInfoVO);
					UserCounterVO userCounterVO = userCounterDAO.getUserCounter(amplifyInfoVO.getAmplifierId());
					if(null!=userCounterVO && userCounterVO.getAmplified()>0){
						userCounterVO.setAmplified(userCounterVO.getAmplified()-1);
						userCounterDAO.updateUserCounter(userCounterVO);
					}
				}
				
			}
			
		}else if ("updateprofile".equalsIgnoreCase(command)) {
			logger.debug("doTask() :: updateprofile :: creatorId =" + tapClipVO.getCreatorId()+", fileId :: "+tapClipVO.getUserFileId());
			if (tapClipVO != null && tapClipVO.getUserFileId() != null && tapClipVO.getCreatorId() != null) {
				mongoServiceImpl.updateClipInfo(tapClipVO);
			}
		}
	}
}
