package com.voizd.service.stats.bo;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voizd.common.beans.vo.CommentCounterVO;
import com.voizd.common.beans.vo.ContentCounterVO;
import com.voizd.common.beans.vo.ContentVO;
import com.voizd.common.beans.vo.StationCounterVO;
import com.voizd.common.beans.vo.StatsVO;
import com.voizd.common.beans.vo.StreamCounterVO;
import com.voizd.common.beans.vo.StreamVO;
import com.voizd.common.constant.ClientStatsConstant;
import com.voizd.dao.exception.DataAccessFailedException;
import com.voizd.dao.exception.DataUpdateFailedException;
import com.voizd.dao.modules.comment.CommentCounterDAO;
import com.voizd.dao.modules.content.ContentCounterDAO;
import com.voizd.dao.modules.content.ContentDAO;
import com.voizd.dao.modules.stationcounter.StationCounterDAO;
import com.voizd.dao.modules.stats.ClientStatsDAO;
import com.voizd.dao.modules.stream.UserStreamCounterDAO;
import com.voizd.dao.modules.stream.UserStreamDAO;
import com.voizd.framework.exception.util.ErrorCodesEnum;
import com.voizd.service.stats.exception.ClientStatServiceException;

public class ClientStatBOImpl implements ClientStatBO {
	private static Logger logger = LoggerFactory.getLogger(ClientStatBOImpl.class);
	private ContentCounterDAO contentCounterDAO;
	private StationCounterDAO stationCounterDAO;
	private ClientStatsDAO clientStatsDAO ;
	private UserStreamCounterDAO userStreamCounterDAO ;
	private UserStreamDAO userStreamDAO ;
	private ContentDAO contentDAO;
	private CommentCounterDAO commentCounterDAO ;
	
	public void setCommentCounterDAO(CommentCounterDAO commentCounterDAO) {
		this.commentCounterDAO = commentCounterDAO;
	}
	public void setContentDAO(ContentDAO contentDAO) {
		this.contentDAO = contentDAO;
	}
	@SuppressWarnings("unchecked")
	public void saveAppStats(Map<String, Object> statsMap) throws ClientStatServiceException{
		logger.debug("statsMap---"+statsMap);
		if( null!=statsMap && null!=statsMap.get(ClientStatsConstant.PLAY)){
			List<StatsVO> statsList = (List<StatsVO>)statsMap.get(ClientStatsConstant.PLAY);
			logger.debug("statsList---"+statsList);
  			for(StatsVO statsVO:statsList){
  				try {
					ContentCounterVO contentCounterVO = contentCounterDAO.getContentCounter(statsVO.getCntId());
					if(null!=contentCounterVO){
						contentCounterVO.setView(contentCounterVO.getView()+1);
						contentCounterDAO.updateContentCounter(contentCounterVO);
					}
					logger.debug("statsVO. stationId---"+statsVO.getStationId());
					
					if( null==statsVO.getStationId() || statsVO.getStationId()==0){
						logger.debug("statsVO.getCntId()---"+statsVO.getCntId());
						ContentVO contentVO = contentDAO.getContentByContentId(statsVO.getCntId());
						logger.debug("statsVO.getStationId()---"+contentVO);
						if(null!=contentVO){
							statsVO.setStationId(contentVO.getStationId());
						}
					}
					StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(statsVO.getStationId());
					logger.debug("statsVO.getStationId()---"+statsVO.getStationId() +stationCounterVO);
					if(null!=stationCounterVO){
						stationCounterVO.setView(stationCounterVO.getView()+1);
						stationCounterDAO.updateStationCounter(stationCounterVO);
					}
					try {
						clientStatsDAO.saveListenStats(statsVO);
					}  catch (DataUpdateFailedException e) {
						logger.error("saveClientStats., individual stats="+statsVO+" DataUpdateFailedException="+e.getLocalizedMessage(), e);
						
					}
					
				} catch (DataAccessFailedException e) {
					logger.error("saveClientStats., DataAccessFailedException="+e.getLocalizedMessage(), e);
					throw new ClientStatServiceException(ErrorCodesEnum.STATION_COUNTER_FAILED_EXCEPTION);	
				} catch (DataUpdateFailedException e) {
					logger.error("saveClientStats., DataUpdateFailedException="+e.getLocalizedMessage(), e);
					throw new ClientStatServiceException(ErrorCodesEnum.STATION_COUNTER_FAILED_EXCEPTION);
				}
  			 }
		}
		logger.debug("statsMap-2--"+statsMap);
		if( null!=statsMap && null!=statsMap.get(ClientStatsConstant.COMMENT_PLAY)){
			List<StatsVO> statsList = (List<StatsVO>)statsMap.get(ClientStatsConstant.COMMENT_PLAY);
			logger.debug("statsList---"+statsList);
  			for(StatsVO statsVO:statsList){
  				try {
  					CommentCounterVO commentCounterVO = commentCounterDAO.getCommentCounter(statsVO.getCmtId());
  					logger.debug("statsVO.commentCounterVO()---"+commentCounterVO+"---"+statsVO.getCmtId());
					if(null!=commentCounterVO){
						commentCounterVO.setView(commentCounterVO.getView()+1);
						commentCounterDAO.updateCommentCounter(commentCounterVO);
					}
					
					try {
						clientStatsDAO.saveCommentListenStats(statsVO);
					}  catch (DataUpdateFailedException e) {
						logger.error("saveClientStats., individual stats="+statsVO+" DataUpdateFailedException="+e.getLocalizedMessage(), e);
						
					}
					
				} catch (DataAccessFailedException e) {
					logger.error("saveClientStats., DataAccessFailedException="+e.getLocalizedMessage(), e);
					throw new ClientStatServiceException(ErrorCodesEnum.STATION_COUNTER_FAILED_EXCEPTION);	
				} catch (DataUpdateFailedException e) {
					logger.error("saveClientStats., DataUpdateFailedException="+e.getLocalizedMessage(), e);
					throw new ClientStatServiceException(ErrorCodesEnum.STATION_COUNTER_FAILED_EXCEPTION);
				}
  			 }
		}
		
		
		if(null!=statsMap && null!=statsMap.get(ClientStatsConstant.SHARE)){
			List<StatsVO> statsList =(List<StatsVO>)statsMap.get(ClientStatsConstant.SHARE);
  			for(StatsVO statsVO:statsList){
  				try {
  					if(null==statsVO.getOp()){
  						statsVO.setOp(ClientStatsConstant.CONTENT_SHARING)	;
  					}
  					if(ClientStatsConstant.CONTENT_SHARING.equalsIgnoreCase(statsVO.getOp())){
						ContentCounterVO contentCounterVO =contentCounterDAO.getContentCounter(statsVO.getCntId());
						if(null!=contentCounterVO){
							contentCounterVO.setShare(contentCounterVO.getShare()+1);
							contentCounterDAO.updateContentCounter(contentCounterVO);
						}	
  					}else if(ClientStatsConstant.STATION_SHARING.equalsIgnoreCase(statsVO.getOp())){
						StationCounterVO stationCounterVO = stationCounterDAO.getStationCounter(statsVO.getStationId());
						if(null!=stationCounterVO){
							stationCounterVO.setShare(stationCounterVO.getShare()+1);
							stationCounterDAO.updateStationCounter(stationCounterVO);
						}	
  					}else if(ClientStatsConstant.STREAM_SHARING.equalsIgnoreCase(statsVO.getOp())){
  						logger.debug("statsVO.getStId()--"+statsVO.getStId()+",statsVO.getCId() "+statsVO.getCreatorId() );
  						if(statsVO.getStId()==null && statsVO.getCreatorId()>0){
  							List<StreamVO> streamVOList = userStreamDAO.getUserStreamList(statsVO.getCreatorId());
  							if(null!=streamVOList && streamVOList.size()>0){
  								StreamVO streamVO = streamVOList.get(0);
  								if(null!= streamVO){
  									statsVO.setStId(streamVO.getStreamId());
  								}
  							}
  						}
  						StreamCounterVO streamCounterVO = userStreamCounterDAO.getStreamCounter(statsVO.getStId());
						if(null!=streamCounterVO){
							streamCounterVO.setShare(streamCounterVO.getShare()+1);
							userStreamCounterDAO.updateStreamCounter(streamCounterVO);
						}	
  					}
  					
  					try {
						clientStatsDAO.saveShareStats(statsVO);
					}  catch (DataUpdateFailedException e) {
						logger.error("saveClientStats., individual share stats="+statsVO+" DataUpdateFailedException="+e.getLocalizedMessage(), e);
						
					}
				}catch (DataAccessFailedException e) {
					logger.error("saveClientStats.,   DataUpdateFailedException="+e.getLocalizedMessage(), e);
					throw new ClientStatServiceException(ErrorCodesEnum.STATION_COUNTER_FAILED_EXCEPTION);	
				}  catch (DataUpdateFailedException e) {
					logger.error("saveClientStats., DataUpdateFailedException="+e.getLocalizedMessage(), e);
					throw new ClientStatServiceException(ErrorCodesEnum.STATION_COUNTER_FAILED_EXCEPTION);
				}
  			 }
		}
	
	}
	public void setContentCounterDAO(ContentCounterDAO contentCounterDAO) {
		this.contentCounterDAO = contentCounterDAO;
	}
	public void setStationCounterDAO(StationCounterDAO stationCounterDAO) {
		this.stationCounterDAO = stationCounterDAO;
	}
	public void setClientStatsDAO(ClientStatsDAO clientStatsDAO) {
		this.clientStatsDAO = clientStatsDAO;
	}
	public void setUserStreamCounterDAO(UserStreamCounterDAO userStreamCounterDAO) {
		this.userStreamCounterDAO = userStreamCounterDAO;
	}
	public void setUserStreamDAO(UserStreamDAO userStreamDAO) {
		this.userStreamDAO = userStreamDAO;
	}
	
}
