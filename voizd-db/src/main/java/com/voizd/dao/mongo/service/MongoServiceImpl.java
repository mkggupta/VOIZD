
package com.voizd.dao.mongo.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.MongoOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.voizd.common.beans.vo.AmplifyInfoVO;
import com.voizd.common.beans.vo.MapVO;
import com.voizd.common.beans.vo.MongoServer;
import com.voizd.common.beans.vo.TagCloudVO;
import com.voizd.common.beans.vo.TagResultVO;
import com.voizd.common.beans.vo.TapClipVO;
import com.voizd.dao.entities.EarthInfo;
import com.voizd.dao.entities.MapInfo;
import com.voizd.dao.modules.mongo.MongoDAO;
import com.voizd.dao.mongo.helper.ClipInfo;
import com.voizd.dao.mongo.helper.MongoHelper;
import com.voizd.dao.service.ServiceFactory;



public class MongoServiceImpl implements MongoService {
	private DBCollection tapCollection = null;
	private DBCollection clipCollection = null;
	private DBCollection iFollowCollection = null;
	private DBCollection tagCollection = null;
	private DBCollection myCollection = null;
    private static String SEPARATOR =":"; 
	private volatile static MongoServiceImpl mongoServicempl;
	private MongoDAO mongoDAO;
	private static final Logger logger = LoggerFactory.getLogger(MongoServiceImpl.class);
	
	private MongoServiceImpl() throws MongoException{
		mongoDAO = ServiceFactory.getService(MongoDAO.class);
		setUpUserTapInfo();
		setClipInfo();
		setIFollowClipInfo();
		setTagCloudInfo();
		setMyStreamInfo();
	}
	
	
	private void setUpUserTapInfo() throws  MongoException {
		ArrayList<ServerAddress> serverList = getServerList("tap");
		MongoClient mongoClient = new MongoClient(serverList,new MongoClientOptions.Builder().build());
		mongoClient.setWriteConcern(WriteConcern.SAFE);
		mongoClient.setWriteConcern(WriteConcern.UNACKNOWLEDGED);
		DB db = mongoClient.getDB("voizddb");
		tapCollection = db.getCollection("vstream");
		if(tapCollection == null){
			logger.error("MongoDB setUpUserTapInfo :: tapCollection  is "+tapCollection);
			throw new RuntimeException("MongoDB connection not found .Please check config ");
		}
    }
	private void setClipInfo() throws  MongoException {
		ArrayList<ServerAddress> serverList = getServerList("clip");
		MongoClient mongoClient = new MongoClient(serverList,new MongoClientOptions.Builder().build());
		mongoClient.setWriteConcern(WriteConcern.SAFE);
		mongoClient.setWriteConcern(WriteConcern.UNACKNOWLEDGED);
		DB db = mongoClient.getDB("voizddb");
		clipCollection = db.getCollection("voizdclip");
		if(clipCollection == null){
			logger.error("MongoDB setUpUserTapInfo :: clipCollection  is "+clipCollection);
			throw new RuntimeException("MongoDB connection not found .Please check config ");
		}
    }
	private void setIFollowClipInfo() throws  MongoException {
		ArrayList<ServerAddress> serverList = getServerList("ifollow");
		MongoClient mongoClient = new MongoClient(serverList,new MongoClientOptions.Builder().build());
		mongoClient.setWriteConcern(WriteConcern.SAFE);
		mongoClient.setWriteConcern(WriteConcern.UNACKNOWLEDGED);
		DB db = mongoClient.getDB("voizddb");
		iFollowCollection = db.getCollection("ifollow");
		if(iFollowCollection == null){
			logger.error("MongoDB setUpUserTapInfo :: iFollowCollection  is "+iFollowCollection);
			throw new RuntimeException("MongoDB connection not found .Please check config ");
		}
    }
	
	private void setTagCloudInfo() throws  MongoException {
		ArrayList<ServerAddress> serverList = getServerList("taginfo");
		MongoClient mongoClient = new MongoClient(serverList,new MongoClientOptions.Builder().build());
		mongoClient.setWriteConcern(WriteConcern.SAFE);
		mongoClient.setWriteConcern(WriteConcern.UNACKNOWLEDGED);
		DB db = mongoClient.getDB("voizddb");
		tagCollection = db.getCollection("taginfo");
		if(tagCollection == null){
			logger.error("MongoDB setUpUserTapInfo :: tagCollection  is "+tagCollection);
			throw new RuntimeException("MongoDB connection not found .Please check config ");
		}
    }
	private void setMyStreamInfo() throws  MongoException {
		ArrayList<ServerAddress> serverList = getServerList("taginfo");
		MongoClient mongoClient = new MongoClient(serverList,new MongoClientOptions.Builder().build());
		mongoClient.setWriteConcern(WriteConcern.SAFE);
		mongoClient.setWriteConcern(WriteConcern.UNACKNOWLEDGED);
		DB db = mongoClient.getDB("voizddb");
		myCollection = db.getCollection("mystream");
		if(myCollection == null){
			logger.error("MongoDB setUpUserTapInfo :: tagCollection  is "+myCollection);
			throw new RuntimeException("MongoDB connection not found .Please check config ");
		}
    }
	private ArrayList<ServerAddress> getServerList(String type){
		ArrayList<ServerAddress> serverList = new ArrayList<ServerAddress>();
		
		try{
			List<MongoServer>  mongoServers = mongoDAO.getMongoServers();
			  for(MongoServer mongoServer : mongoServers){
					StringBuilder serverIp= new StringBuilder();
					 logger.debug(" getServerList () :: mongoServer :: "+mongoServer.getHost()+" , mongoServer :: "+mongoServer.getPort()); 
					serverIp.append(mongoServer.getHost()).append(SEPARATOR).append(mongoServer.getPort());
				ServerAddress serverAddress = new ServerAddress(serverIp.toString());
			    serverList.add(serverAddress);
				}
			   }catch (Exception e) {
				   logger.error("Erroe while get server list " + e.getLocalizedMessage(),e);
				}
				return serverList;
		}
	public static MongoServiceImpl getInstance() throws UnknownHostException, MongoException {
		if (mongoServicempl == null) {
			synchronized (MongoServiceImpl.class) {
				if (mongoServicempl == null) {
					mongoServicempl = new MongoServiceImpl();
				}
			}
		}
		return mongoServicempl;
	}

	
	private void insertUserVStream(TapClipVO tapClipVO,Long userId) throws MongoException{
		BasicDBObject vStream = new BasicDBObject();
		vStream.put("_id",userId);
		vStream.put("username",tapClipVO.getUsername());
		DBObject dbo;
		ArrayList<DBObject> array = new ArrayList<DBObject>();
		logger.debug("insertUserVStream tapClipVO.getCreatorId() :: "+tapClipVO.getCreatorId());
		logger.debug("insertUserVStream tapClipVO.getUsername() :: "+tapClipVO.getUsername());
		dbo = MongoHelper.bsonFromPojoForTap(tapClipVO);
		array.add(dbo);
		logger.debug("insertUserVStream tapClipVO.dbo() :: "+dbo.toString());
		vStream.put("clipinfo", array);	
		
		tapCollection.save(vStream);
	}
	private void updatedUserVStream(TapClipVO tapClipVO) throws MongoException {
		BasicDBObject match = new BasicDBObject();
		match.put("_id", tapClipVO.getCreatorId());
	    //	match.put("username", tapClipVO.getUsername());
		logger.debug("updatetUserVStream creatorid :: "+tapClipVO.getCreatorId());
		//logger.debug("updatetUserVStream tapClipVO.getUsername() :: "+tapClipVO.getUsername());
		BasicDBObject clipinfo  = (BasicDBObject) MongoHelper.bsonFromPojoForTap(tapClipVO);
		BasicDBObject update = new BasicDBObject();
		update.put("$push", new BasicDBObject("clipinfo", clipinfo));
		tapCollection.update(match, update);

	}

	private void insertVStreamClip(TapClipVO tapClipVO) throws MongoException{
		DBObject dboClip = MongoHelper.bsonFromPojoForClip(tapClipVO);
		clipCollection.insert(dboClip);
	}
	
	public void upsertMyStreamClip(TapClipVO tapClipVO) throws MongoException {
		BasicDBObject match = new BasicDBObject();
		match.put("_id", tapClipVO.getCreatorId());
		DBObject dbObject = myCollection.findOne(match);
		logger.debug("upsertMyStreamClip  creatorid :: "+ tapClipVO.getCreatorId()+" , dbObject :: "+dbObject);
		if (dbObject != null) {
			logger.debug("upsertMyStreamClip   push :: ");
			BasicDBObject clipinfo = (BasicDBObject) MongoHelper.bsonFromPojoForMyClip(tapClipVO);
			BasicDBObject update = new BasicDBObject();
			update.put("$addToSet", new BasicDBObject("clipinfo", clipinfo));
			myCollection.update(match, update);
		} else {
			ArrayList<DBObject> array = new ArrayList<DBObject>();
			DBObject dboClip = MongoHelper.bsonFromPojoForMyClip(tapClipVO);
			array.add(dboClip);
			match.put("clipinfo", array);
			logger.debug("upsertMyStreamClip   insert :: ");
			myCollection.insert(match);
		}
	}
	
	public void upsertAmplifyMyStreamClip(TapClipVO tapClipVO) throws MongoException {
		BasicDBObject match = new BasicDBObject();
		match.put("_id", tapClipVO.getAmplifierId());
		DBObject dbObject = myCollection.findOne(match);
		logger.debug("upsertMyStreamClip  creatorid :: "+ tapClipVO.getCreatorId());
		if (dbObject != null) {
			BasicDBObject clipinfo = (BasicDBObject) MongoHelper.bsonFromPojoForMyAmplifyClip(tapClipVO);
			BasicDBObject update = new BasicDBObject();
			update.put("$addToSet", new BasicDBObject("clipinfo", clipinfo));
			myCollection.update(match, update);
		} else {
			ArrayList<DBObject> array = new ArrayList<DBObject>();
			DBObject dboClip = MongoHelper.bsonFromPojoForMyAmplifyClip(tapClipVO);
			array.add(dboClip);
			match.put("clipinfo", array);
			myCollection.insert(match);
		}
	}

	@Override
	public void insertVStream(TapClipVO tapClipVO,Long userId) throws MongoException {
		BasicDBObject match = new BasicDBObject();
		match.put("_id", tapClipVO.getCreatorId());
		DBObject dbObject= tapCollection.findOne(match);
		logger.debug("MongoDB insertVStream :: dbObject :: "+dbObject+" isRegistration :: "+tapClipVO.isRegistration());
		if(tapClipVO.isRegistration() || dbObject == null ){
			logger.debug("MongoDB insertUserVStream :: ");
			insertUserVStream(tapClipVO,userId);
		}else{
			logger.debug("MongoDB updatedUserVStream :: "+tapClipVO);
			updatedUserVStream(tapClipVO);
		}
		insertVStreamClip(tapClipVO);
		upsertMyStreamClip(tapClipVO);
	}
	
	
/*	@Override
	public void upsertAmplifyVStream(TapClipVO tapClipVO,Long userId) throws MongoException{
		BasicDBObject match = new BasicDBObject();
		match.put("_id",userId);
		//match.put("clipinfo.contentid",tapClipVO.getContentId());
		DBObject dbObject= tapCollection.findOne(match);
		logger.debug("MongoDB upsertAmplifyVStream :: "+dbObject);
		if(dbObject != null ){
			//DBObject dbo = MongoHelper.bsonFromAmplifyByUserId(tapClipVO.getAmplifierId());
			DBObject dbo = MongoHelper.bsonFromPojoForAmplify(tapClipVO);
			BasicDBObject update = new BasicDBObject();
			update.put("$addToSet",new BasicDBObject("clipinfo", dbo));
			//update.put("$push", new BasicDBObject("clipinfo.$.mp",dbo));
			tapCollection.update(match, update);
		}else{
			addVStreamByAmplify(tapClipVO,userId);
			logger.debug("MongoDB upsertAmplifyVStream :: addVStreamByAmplify "+userId);
		}
	}*/
	
	@Override
	public void upsertAmplifyVStream(TapClipVO tapClipVO, Long userId)
			throws MongoException {
		DBObject matchOne = new BasicDBObject("$match", new BasicDBObject(
				"_id", userId));
		DBObject matchTwo = new BasicDBObject("$match", new BasicDBObject(
				"clipinfo.contentid", tapClipVO.getContentId()));
		DBObject unwind = new BasicDBObject("$unwind", "$clipinfo");
		Iterable<DBObject> output = tapCollection.aggregate(matchOne, unwind,
				matchTwo).results();
		logger.debug("MongoDB upsertAmplifyVStream :: output :: " + output);
		if (output != null && output.iterator().hasNext()) {
			logger.debug("MongoDB upsertAmplifyVStream :: true :: " );
			for (DBObject clip : output) {
				logger.debug("MongoDB upsertAmplifyVStream :: update :: " );
				DBObject clipInfo = (DBObject) clip.get("clipinfo");
				DBObject dbo = MongoHelper.bsonFromPojoFormAmplify(clipInfo,tapClipVO);
				
				BasicDBObject match = new BasicDBObject();
				match.put("_id", userId);
				match.put("clipinfo.contentid",tapClipVO.getContentId());
    			
				BasicDBObject update = new BasicDBObject();
				update.put("$set",dbo);
				tapCollection.update(match, update);
			}
		} else {
			addVStreamByAmplify(tapClipVO, userId);
			logger.debug("MongoDB upsertAmplifyVStream :: addVStreamByAmplify "
					+ userId);
		}
	}

	private void addVStreamByAmplify(TapClipVO tapClipVO,Long userId) throws MongoException {
		BasicDBObject vStream = new BasicDBObject();
		vStream.put("_id",userId);
	
		DBObject dbo = MongoHelper.bsonFromPojoForAmplify(tapClipVO);
		BasicDBObject update = new BasicDBObject();
		update.put("$push", new BasicDBObject("clipinfo", dbo));
		
		tapCollection.update(vStream, update);

	//	logger.debug("MongoDB addVStreamByAmplify :: vStream "+array);
	}

	@Override
	public HashMap<String, Object>  getUserVStream(Long userId, String type,int slice) throws MongoException {
		  BasicDBObject newQuery = new BasicDBObject();
		  HashMap<String, Object> dataMap = new HashMap<String, Object>();
		  newQuery.put("_id", userId);
		  int count = getVStreamCount(userId);
		  logger.debug("MongoDB getUserVStream :: count  :: "+count+" , slice :: "+slice);
		  if(count ==0){
			  dataMap.put("count", count);
				 return dataMap; 
			  }
		  
		 int pageSize =10;
		  if(count < pageSize){
			  pageSize = count;
			  slice = count;
		  }else if(count < slice ){		 
			  pageSize = slice-count;
			  pageSize =10-pageSize;
			  slice=count;
		  }else if(count>slice){
			  pageSize=10;
		  }
		  int[] anArray = new int[2];
			 anArray[0] = -slice;
		     anArray[1] = pageSize;
		     
		     logger.debug("MongoDB getUserVStream :: count  :: "+count+" , slice :: "+slice+" ,pageSize :: "+pageSize);
		     
		  BasicDBObject projection = new BasicDBObject("clipinfo", new BasicDBObject("$slice", anArray));
		  DBObject  cursors = tapCollection.findOne(newQuery,projection);
		  logger.debug("MongoDB getUserVStream :: userId :: "+userId);
		  ArrayList<Long> contentList = new ArrayList<Long>();
		  String userName =null;
		 
		 /// logger.error("MongoDB getUserVStream :: cursors count :: "+cursors.);
		  HashMap<Long, Object> clipInfoMap = new HashMap<Long, Object>();
		  if(cursors != null) {
		/*	  BasicDBObject basicDBObject = (BasicDBObject) cursors.next();
			  logger.error("MongoDB getUserVStream :: basicDBObject :: "+basicDBObject);*/
			  userName = (String) cursors.get("username");
			  logger.debug("MongoDB getUserVStream :: userName :: "+userName);
			  BasicDBList clipinfo = (BasicDBList) cursors.get("clipinfo");
				
				for (Iterator<Object> it = clipinfo.iterator(); it.hasNext();) {
					BasicDBObject dbo = (BasicDBObject) it.next();
					ClipInfo clipInfo =  MongoHelper.makeClipInfoPojoFromBsonMyStream(dbo);
					contentList.add(clipInfo.getContentId());
					clipInfoMap.put(clipInfo.getContentId(),clipInfo);
				}
		  }
		  List<TapClipVO> tapClipVOList = new ArrayList<TapClipVO>();
		  DBObject obj = new BasicDBObject ("contentid", new BasicDBObject("$in",contentList));
		  List<TapClipVO> resultList = new ArrayList<TapClipVO>();
		  if(contentList.size()>0){
			 // logger.error("MongoDB getUserVStream :: contentList.size() :: "+contentList.size()+", contentList :: "+contentList);
		  DBObject sortFields = new BasicDBObject("contentid",-1);
		  DBCursor  cursorsClip = clipCollection.find(obj).sort(sortFields);
		 
		  while (cursorsClip.hasNext()) {
			  BasicDBObject basicDBObject = (BasicDBObject) cursorsClip.next();
			  TapClipVO tapClipVO = MongoHelper.makeClipDetailsPojoFromBson(basicDBObject);
			  tapClipVO.setUsername(userName);
			  tapClipVOList.add(tapClipVO);
		  }
		
			for (TapClipVO tapClipVO : tapClipVOList) {
				ClipInfo clipInfo = (ClipInfo) clipInfoMap.get(tapClipVO.getContentId());
				if (clipInfo != null) {
					tapClipVO.setAmplify(clipInfo.isAmplify());
					tapClipVO.setAmplifierId(clipInfo.getAmplifierId());
				}
				resultList.add(tapClipVO);
			}
		  
		 }
		  
		  dataMap.put("content", resultList);
		  dataMap.put("count", count);
		  logger.debug("MongoDB getUserVStream :: tapClipVOList :: "+tapClipVOList);
		return dataMap;
	}


/*	@Override
	public HashMap<String, Object> getMyVStream(Long userId, String type,int skip) throws MongoException {
		  HashMap<String, Object> dataMap = new HashMap<String, Object>();
		  List<TapClipVO> tapClipVOList = new ArrayList<TapClipVO>();
		  BasicDBObject newQuery = new BasicDBObject();
		  newQuery.put("creatorid", userId);
		  DBObject sortFields = new BasicDBObject("contentid",-1);
		  DBCursor  cursorsClip = clipCollection.find(newQuery).sort(sortFields).limit(10).skip(skip);
		  int count = cursorsClip.count();
		  logger.debug("MongoDB getMyVStream :: count :: "+count);
		  while (cursorsClip.hasNext()) {
			  BasicDBObject basicDBObject = (BasicDBObject) cursorsClip.next();
			  TapClipVO tapClipVO = MongoHelper.makeClipDetailsPojoFromBson(basicDBObject);
			  tapClipVOList.add(tapClipVO);
		  }
		  dataMap.put("content", tapClipVOList);
		  dataMap.put("count", count);
		return dataMap;
	}*/
	
	public HashMap<String, Object> getMyVStream(Long userId, String type, int slice) throws MongoException {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		BasicDBObject myStream = new BasicDBObject();
		myStream.put("_id", userId);
		
		int count  = getMyVStreamCount(userId);
		  logger.debug("MongoDB getMyVStream :: count :: "+count+", slice :: "+slice);
		  
		  if(count < 1){
			  dataMap.put("count", count);
			return dataMap;  
		  }

		 int pageSize =10;
		  if(count < pageSize){
			  pageSize = count;
			  slice = count;
		  }else if(count < slice ){		 
			  pageSize = slice-count;
			  pageSize =10-pageSize;
			  slice=count;
		  }else if(count>slice){
			  pageSize=10;
		  }
		  int[] anArray = new int[2];
			 anArray[0] = -slice;
		     anArray[1] = pageSize;
		
		BasicDBObject projection = new BasicDBObject("clipinfo", new BasicDBObject("$slice", anArray));
		DBObject cursors = myCollection.findOne(myStream, projection);
	logger.debug("MongoDB getUserMYStream :: count  :: "+count+" , slice :: "+slice+" ,pageSize :: "+pageSize);
		ArrayList<Long> contentList = new ArrayList<Long>();
		//ArrayList<ClipInfo> clipInfoList = new ArrayList<ClipInfo>();
		HashMap<Long, Object> clipInfoMap = new HashMap<Long, Object>();
		if (cursors != null) {
			BasicDBList clipinfo = (BasicDBList) cursors.get("clipinfo");
			for (Iterator<Object> it = clipinfo.iterator(); it.hasNext();) {
				BasicDBObject dbo = (BasicDBObject) it.next();
				//ClipInfo clipInfo = MongoHelper.makeClipInfoPojoFromBsonMyStream(dbo);
				ClipInfo clipInfo =  MongoHelper.makeClipInfoPojoFromBson(dbo);
				contentList.add(clipInfo.getContentId());
				clipInfoMap.put(clipInfo.getContentId(),clipInfo);
				//clipInfoList.add(clipInfo);
			}
		}

		List<TapClipVO> tapClipVOList = new ArrayList<TapClipVO>();
		DBObject obj = new BasicDBObject("contentid", new BasicDBObject("$in", contentList));
		HashMap<Long, TapClipVO> tempMap = new HashMap<Long, TapClipVO>();
		if (contentList.size() > 0) {
			logger.error("MongoDB getUserVStream :: contentList) :: " + contentList);
			DBObject sortFields = new BasicDBObject("contentid", -1);
			//DBCursor cursorsClip = clipCollection.find(obj);//.sort(sortFields);
			DBCursor cursorsClip = clipCollection.find(obj).sort(sortFields);
			while (cursorsClip.hasNext()) {
				BasicDBObject basicDBObject = (BasicDBObject) cursorsClip.next();
				//logger.error("MongoDB basicDBObject :: basicDBObject"+basicDBObject);
				TapClipVO tapClipVO = MongoHelper.makeClipDetailsPojoFromBson(basicDBObject);
				//logger.error("MongoDB tapClipVO :: tapClipVO :: " + tapClipVO.getContentId());
				tempMap.put(tapClipVO.getContentId(), tapClipVO);
				//tapClipVOList.add(tapClipVO);
			}
		}
		for(Long contentId : contentList){
			tapClipVOList.add(tempMap.get(contentId));
		}
		List<TapClipVO> resultList = new ArrayList<TapClipVO>();
		for(TapClipVO tapClipVO: tapClipVOList){
			ClipInfo clipInfo  = (ClipInfo) clipInfoMap.get(tapClipVO.getContentId());
			//logger.error("MongoDB getUserVStream :: tapClipVOList :: ContentId :: " + tapClipVO.getContentId());
			if (clipInfo != null) {
				tapClipVO.setAmplify(clipInfo.isAmplify());
				tapClipVO.setAmplifierId(clipInfo.getAmplifierId());
			}
			resultList.add(tapClipVO);
		}
		//
		Collections.reverse(resultList);
		
		dataMap.put("content", resultList);
		dataMap.put("count", count);
		return dataMap;
	}

	
	private int getVStreamCount(Long userId){
		  BasicDBObject newQuery = new BasicDBObject();
		  newQuery.put("_id", userId);
		  DBObject  cursors  = tapCollection.findOne(newQuery);
		  int count=0;
		  if(cursors != null) {
			  BasicDBList clipinfo = (BasicDBList) cursors.get("clipinfo");
				count =  clipinfo.size();
		  }
		  return count;
	}

	private int getMyVStreamCount(Long userId){
		  BasicDBObject newQuery = new BasicDBObject();
		  newQuery.put("_id", userId);
		  DBObject  cursors  = myCollection.findOne(newQuery);
		  int count=0;
		  if(cursors != null) {
			  BasicDBList clipinfo = (BasicDBList) cursors.get("clipinfo");
				count =  clipinfo.size();
		  }
		  return count;
	}

	@Override
	public void updateFollowerVStream(TapClipVO tapClipVO, Long userId) throws MongoException {
		BasicDBObject match = new BasicDBObject();
		match.put("_id", userId);
		logger.debug("updateFollowerVStream ::CreatorId ::  "+tapClipVO.getCreatorId());
		logger.debug("updateFollowerVStream tapClipVO.getUsername() :: "+tapClipVO.getUsername());
		BasicDBObject clipinfo  = (BasicDBObject) MongoHelper.bsonFromPojoForTap(tapClipVO);
		BasicDBObject update = new BasicDBObject();
		update.put("$push", new BasicDBObject("clipinfo", clipinfo));
		tapCollection.update(match, update);
	}


	@Override
	public void upsertIFollowerVStream(TapClipVO tapClipVO, Long userId) throws MongoException {
		BasicDBObject baseObj = new BasicDBObject();
		baseObj.put("_id", userId);
		DBObject dbObject= iFollowCollection.findOne(baseObj);
		if(dbObject == null){
			insertIFollowVStream(tapClipVO,userId);
		}else{
		BasicDBObject match = new BasicDBObject();
		match.put("_id", userId);
		logger.debug("upsertIFollowerVStream ::CreatorId ::  "+tapClipVO.getCreatorId()+" , userId ::"+userId);
		BasicDBObject clipinfo  = (BasicDBObject) MongoHelper.bsonFromPojoForTap(tapClipVO);
		BasicDBObject update = new BasicDBObject();
		update.put("$push", new BasicDBObject("clipinfo", clipinfo));
		iFollowCollection.update(match, update);
		}
	}
	
	private void insertIFollowVStream(TapClipVO tapClipVO,Long userId) throws MongoException{
		BasicDBObject vStream = new BasicDBObject();
		vStream.put("_id",userId);
		DBObject dbo;
		ArrayList<DBObject> array = new ArrayList<DBObject>();
		logger.error("insertIFollowVStream :: creatorid :: "+tapClipVO.getCreatorId()+", userid :: "+userId);
		dbo = MongoHelper.bsonFromPojoForTap(tapClipVO);
		array.add(dbo);
		vStream.put("clipinfo", array);	
		iFollowCollection.save(vStream);
	}


	@Override
	public HashMap<String, Object> getIFollowVStream(Long userId, int slice) throws MongoException {
		  BasicDBObject newQuery = new BasicDBObject();
		  HashMap<String, Object> dataMap = new HashMap<String, Object>();
		  newQuery.put("_id", userId);
		  int count = getIFollowVStreamCount(userId);
		  
		  logger.debug("MongoDB getIFollowVStream :: count  :: "+count+" , slice :: "+slice);
		  if(count ==0){
			  dataMap.put("count", count);
			 return dataMap; 
		  }
		  int pageSize =10;
		  if(count < pageSize){
			  pageSize = count;
			  slice = count;
		  }else if(count < slice ){		 
			  pageSize = slice-count;
		  }
		  int[] anArray = new int[2];
			 anArray[0] = -slice;
		     anArray[1] = pageSize;
		     logger.debug("MongoDB getUserVStream :: count  :: "+count+" , slice :: "+slice+" ,pageSize :: "+pageSize);
		  BasicDBObject projection = new BasicDBObject("clipinfo", new BasicDBObject("$slice", anArray));
		  DBObject  cursors = iFollowCollection.findOne(newQuery,projection);
		  logger.debug("MongoDB getIFollowVStream :: userId :: "+userId);
		  ArrayList<Long> contentList = new ArrayList<Long>();
		  String userName =null;
		 
		  if(cursors != null) {
			  userName = (String) cursors.get("username");
			  logger.debug("MongoDB getIFollowVStream :: userName :: "+userName);
			  BasicDBList clipinfo = (BasicDBList) cursors.get("clipinfo");			 
				for (Iterator<Object> it = clipinfo.iterator(); it.hasNext();) {
					BasicDBObject dbo = (BasicDBObject) it.next();
					ClipInfo clipInfo =  MongoHelper.makeClipInfoPojoFromBson(dbo);
					contentList.add(clipInfo.getContentId());
				}
		  }
		  List<TapClipVO> tapClipVOList = new ArrayList<TapClipVO>();
		  logger.debug("MongoDB getIFollowVStream :: contentList :: "+contentList);
		  DBObject obj = new BasicDBObject ("contentid", new BasicDBObject("$in",contentList));
		  if(contentList.size()>0){
			  logger.debug("MongoDB getIFollowVStream :: contentList.size() :: "+contentList.size());
		  DBObject sortFields = new BasicDBObject("contentid",-1);
		  DBCursor  cursorsClip = clipCollection.find(obj).sort(sortFields);
		  while (cursorsClip.hasNext()) {
			  BasicDBObject basicDBObject = (BasicDBObject) cursorsClip.next();
			  TapClipVO tapClipVO = MongoHelper.makeClipDetailsPojoFromBson(basicDBObject);
			  tapClipVO.setUsername(userName);
			  tapClipVOList.add(tapClipVO);
		  }
		 }
		  dataMap.put("content", tapClipVOList);
		  dataMap.put("count", count);
		  logger.debug("MongoDB getUserVStream :: tapClipVOList :: "+tapClipVOList);
		return dataMap;
	}
	
	private int getIFollowVStreamCount(Long userId){
		  BasicDBObject newQuery = new BasicDBObject();
		  newQuery.put("_id", userId);
		  DBObject  cursors  = iFollowCollection.findOne(newQuery);
		  int count=0;
		  if(cursors != null) {
			  BasicDBList clipinfo = (BasicDBList) cursors.get("clipinfo");
				count =  clipinfo.size();
		  }
		  return count;
	}


	@Override
	public void deleteContent(TapClipVO tapClipVO) throws MongoException {
		  BasicDBObject deleteQuery = new BasicDBObject();
		  deleteQuery.put("contentid", tapClipVO.getContentId());
		  deleteQuery.put("creatorid", tapClipVO.getCreatorId());
		  logger.debug("MongoDB deleteContent() :: deleteQuery :: "+deleteQuery);
		  clipCollection.remove(deleteQuery);
	}


/*	@Override
	public void deleteContentFromIFollow(TapClipVO tapClipVO,Long userId) throws MongoException {
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.put("_id", userId);
		BasicDBObject clipinfo = new BasicDBObject();
		clipinfo.put("contentid", tapClipVO.getContentId());
		clipinfo.put("creatorid", tapClipVO.getCreatorId());
		BasicDBObject update = new BasicDBObject();
		update.put("$pull", new BasicDBObject("clipinfo", clipinfo));
		logger.debug("deleteContentFromIFollow() :: update " + update+" , updateQuery :: "+updateQuery);
		iFollowCollection.update(updateQuery, update);
	}*/


	@Override
	public void deleteContentFromVStream(TapClipVO tapClipVO, Long userId) throws MongoException {
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.put("_id", userId);

		BasicDBObject clipinfo = new BasicDBObject();
		clipinfo.put("contentid", tapClipVO.getContentId());
		///clipinfo.put("creatorid", tapClipVO.getCreatorId());
        logger.debug("deleteContentFromVStream() :: userId " + userId+" ,contentid :: "+tapClipVO.getContentId());
		BasicDBObject update = new BasicDBObject();
		update.put("$pull", new BasicDBObject("clipinfo", clipinfo));
		logger.debug("deleteContentFromVStream() :: delete " + update+" ,deleteQuery :: "+updateQuery);
		tapCollection.update(updateQuery, update);
	}


	/*@Override
	public void deleteFromIFollowerVStream(TapClipVO tapClipVO, Long userId) throws MongoException {
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.put("_id", userId);

		BasicDBObject clipinfo = new BasicDBObject();
		clipinfo.put("contentid", tapClipVO.getContentId());
		clipinfo.put("creatorid", tapClipVO.getCreatorId());

		BasicDBObject update = new BasicDBObject();
		update.put("$pull", new BasicDBObject("clipinfo", clipinfo));
		logger.debug("deleteFromIFollowerVStream() :: update " + update+" , deleteQuery :: "+updateQuery);
		iFollowCollection.update(updateQuery, update);
	}


	@Override
	public void deleteFromIUserVStream(TapClipVO tapClipVO, Long userId) throws MongoException {
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.put("_id", userId);

		BasicDBObject clipinfo = new BasicDBObject();
		clipinfo.put("contentid", tapClipVO.getContentId());
		clipinfo.put("creatorid", tapClipVO.getCreatorId());

		BasicDBObject update = new BasicDBObject();
		update.put("$pull", new BasicDBObject("clipinfo", clipinfo));
		logger.debug("deleteFromIUserVStream() :: delete " + update+" , deleteQuery :: "+updateQuery);
		tapCollection.update(updateQuery, update);
	}
*/

	@Override
	public void updateClipInfo(TapClipVO tapClipVO) throws MongoException {
		BasicDBObject clipinfo = new BasicDBObject();
		clipinfo.put("creatorid", tapClipVO.getCreatorId());
		
		BasicDBObject updateInfo = new BasicDBObject();
		updateInfo.put("ufileid", tapClipVO.getUserFileId());
		
		BasicDBObject update = new BasicDBObject();	
		update.put("$set", updateInfo);
		logger.debug("updateClipInfo() :: updateInfo " + updateInfo+" ,clipinfo :: "+clipinfo);
		clipCollection.update(clipinfo, update,false,true);
		
	}


	@Override
	public void insertTagDetail(TagCloudVO tagCloudVO) throws MongoException {
		DBObject dbObject = MongoHelper.bsonFromPojo(tagCloudVO);
		tagCollection.save(dbObject);
	}


	@Override
	public ArrayList<EarthInfo> getTrendingTagList(String country, int limit) throws MongoException {
		ArrayList<EarthInfo> earthInfoList = new ArrayList<EarthInfo>();
		double[] anArray = new double[2];
		anArray[0] = 0.0;
		anArray[1] = 0.0;
		DBObject matchOne = new BasicDBObject("$match", new BasicDBObject("location", anArray));
		DBObject matchTwo = new BasicDBObject("$match", new BasicDBObject("cdate", new BasicDBObject("$lt", new Date())));
		DBObject matchThree = new BasicDBObject("$match", new BasicDBObject("country", "India"));

		DBObject unwind = new BasicDBObject("$unwind", "$tag_cloud");

		DBObject groupFields = new BasicDBObject("_id", "$tag_cloud");
		groupFields.put("count", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject sort = new BasicDBObject();
		sort.put("$sort", new BasicDBObject("count", -1));

		DBObject limitObj = new BasicDBObject();
		limitObj.put("$limit", limit);
		//AggregationOutput output = tagCollection.aggregate(matchOne, matchTwo, matchThree, unwind, group, sort, limitObj);
		AggregationOutput output = tagCollection.aggregate(matchTwo, unwind, group, sort, limitObj);
		EarthInfo earthInfo = null;
		TagResultVO tagResultVO = null;
		for (DBObject obj : output.results()) {
			earthInfo = new EarthInfo();
			String tag = (String) obj.get("_id").toString();
			Gson json = new GsonBuilder().create();
			tagResultVO = json.fromJson(tag, TagResultVO.class);
			int count = (int) obj.get("count");
			earthInfo.setCountry("India");
			earthInfo.setTagName(tagResultVO.getTag());
			earthInfo.setContCnt((long) count);
			earthInfoList.add(earthInfo);
		}
		return earthInfoList;
	}


	@Override
	public void deAmplifyClip(TapClipVO tapClipVO, Long userId) throws MongoException {
		BasicDBObject vStream = new BasicDBObject();
		vStream.put("_id",userId);
		
	}


	@Override
	public void deleteFromMyStream(TapClipVO tapClipVO, Long userId) throws MongoException {
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.put("_id", userId);

		BasicDBObject clipinfo = new BasicDBObject();
		clipinfo.put("contentid", tapClipVO.getContentId());
		//clipinfo.put("creatorid", tapClipVO.getCreatorId());

		BasicDBObject update = new BasicDBObject();
		update.put("$pull", new BasicDBObject("clipinfo", clipinfo));
		logger.debug("deleteFromMyStream() :: userid "+userId+ ",delete "+ update+" ,deleteQuery :: "+updateQuery);
		myCollection.update(updateQuery, update);
	}


	@Override
	public ArrayList<MapInfo> getTagList(String country, int limit) throws MongoException {
		ArrayList<MapInfo> mapInfoList = new ArrayList<MapInfo>();
		double[] anArray = new double[2];
		anArray[0] = 0.0;
		anArray[1] = 0.0;
		DBObject matchOne = new BasicDBObject("$match", new BasicDBObject("location", anArray));
		DBObject matchTwo = new BasicDBObject("$match", new BasicDBObject("cdate", new BasicDBObject("$lt", new Date())));
		DBObject matchThree = new BasicDBObject("$match", new BasicDBObject("country", "India"));

		DBObject unwind = new BasicDBObject("$unwind", "$tag_cloud");

		DBObject groupFields = new BasicDBObject("_id", "$tag_cloud");
		groupFields.put("count", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject sort = new BasicDBObject();
		sort.put("$sort", new BasicDBObject("count", -1));

		DBObject limitObj = new BasicDBObject();
		limitObj.put("$limit", limit);
		//AggregationOutput output = tagCollection.aggregate(matchOne, matchTwo, matchThree, unwind, group, sort, limitObj);
		AggregationOutput output = tagCollection.aggregate(matchTwo, unwind, group, sort, limitObj);
		logger.debug("getTagList() :: output "+output);
		MapInfo mapInfo = null;
		TagResultVO tagResultVO = null;
		for (DBObject obj : output.results()) {
			mapInfo = new MapInfo();
			String tag = (String) obj.get("_id").toString();
			Gson json = new GsonBuilder().create();
			tagResultVO = json.fromJson(tag, TagResultVO.class);
			int count = (int) obj.get("count");
			mapInfo.setCount(count);
			mapInfo.setTag(tagResultVO.getTag());
			mapInfo = getTagInfo(tagResultVO.getTag(),mapInfo);
			mapInfoList.add(mapInfo);
			logger.debug("getTagList() :: obj "+obj);
		}
	
		return mapInfoList;
	}

	private MapInfo getTagInfo(String tag ,MapInfo mapInfo){
		DBObject info = new BasicDBObject("tag_cloud.tag", tag);
		  DBObject  cursors = tagCollection.findOne(info);
		  if(cursors != null){
			 Long creatorId =  (Long) cursors.get("creatorid");
			 mapInfo.setCreatorId(creatorId);
			 Long contentId =  (Long) cursors.get("contentid");
			 mapInfo.setContentId(contentId);
			 String country =  (String) cursors.get("country");
			 mapInfo.setCountry(country);
			 String language =  (String) cursors.get("language");
			 String fileId =  (String) cursors.get("fileId");
			 mapInfo.setFileId(fileId);
			 mapInfo.setLanguage(language);
			 Date date =  (Date) cursors.get("cdate");
			 mapInfo.setCreatedDate(date);
			 BasicDBList location = (BasicDBList) cursors.get("location");
			 for (Iterator<Object> it = location.iterator(); it.hasNext();) {
					BasicDBObject dbo = (BasicDBObject) it.next();
					Double latitude =   (Double) dbo.get("latitude");
					mapInfo.setLatitude(latitude);
					Double longitude =   (Double) dbo.get("longitude");
					mapInfo.setLongitude(longitude);
			 }
		  }
		return mapInfo;
	}
	
		private MapInfo getTagInfoNew(String tag ,MapInfo mapInfo){
		DBObject info = new BasicDBObject("tag_cloud.tag", tag);
	    DBObject sort = new BasicDBObject("_id",-1);
		 // DBObject  cursors = tagCollection.findOne(info);
		  DBCursor  cursors = tagCollection.find(info).sort(sort).limit(1);
		  while (cursors.hasNext()){
			 BasicDBObject basicDBObject = (BasicDBObject) cursors.next();
			 Long creatorId =  (Long) basicDBObject.get("creatorid");
			 mapInfo.setCreatorId(creatorId);
			 Long contentId =  (Long) basicDBObject.get("contentid");
			 mapInfo.setContentId(contentId);
			 String country =  (String) basicDBObject.get("country");
			 mapInfo.setCountry(country);
			 String language =  (String) basicDBObject.get("language");
			 String fileId =  (String) basicDBObject.get("fileId");
			 mapInfo.setFileId(fileId);
			 mapInfo.setLanguage(language);
			 Date date =  (Date) basicDBObject.get("cdate");
			 mapInfo.setCreatedDate(date);
			 BasicDBList location = (BasicDBList) basicDBObject.get("location");
			 for (Iterator<Object> it = location.iterator(); it.hasNext();) {
					BasicDBObject dbo = (BasicDBObject) it.next();
					Double latitude =   (Double) dbo.get("latitude");
					mapInfo.setLatitude(latitude);
					Double longitude =   (Double) dbo.get("longitude");
					mapInfo.setLongitude(longitude);
			 }
		  }
		return mapInfo;
	}
		@Override
		public ArrayList<MapInfo> getMapTagList(MapVO mapVO, int limit) throws MongoException {
			ArrayList<MapInfo> mapInfoList = new ArrayList<MapInfo>();
			double[] anArray = new double[2];
			anArray[0] = mapVO.getLongitude();
			anArray[1] = mapVO.getLatitude();

			DBObject unwind = new BasicDBObject("$unwind", "$tag_cloud");

			DBObject groupFields = new BasicDBObject("_id", "$tag_cloud");
			groupFields.put("count", new BasicDBObject("$sum", 1));
			DBObject group = new BasicDBObject("$group", groupFields);

			DBObject sort = new BasicDBObject();
			sort.put("$sort", new BasicDBObject("count", -1));

			DBObject limitObj = new BasicDBObject();
			limitObj.put("$limit", limit);
			//manish
		
			BasicDBObject taginfo = new BasicDBObject();
			
			taginfo.put("near",anArray);
			taginfo.put("distanceField","dist.calculated");
			taginfo.put("uniqueDocs", true);
			taginfo.put("num",50);
		/*	DBObject matchTwo2 = new BasicDBObject("type","Point");
			DBObject matchTwo4 = new BasicDBObject("coordinates",anArray);
			DBObject matchThree3 = new BasicDBObject("$maxDistance",500);*/
			DBObject matchOne1 = new BasicDBObject("$geoNear",taginfo);
			logger.debug("query-matchOne1-------------"+matchOne1);
			AggregationOutput output = null;
			Date oldDate = subtractDay(new Date());
			DBObject matchTwo = new BasicDBObject("$match", new BasicDBObject("cdate", new BasicDBObject("$lt", new Date()).append("$gt", oldDate)));
			output = tagCollection.aggregate(matchTwo, unwind, group, sort, limitObj);
			if(output == null){
				logger.debug("query-matchOne1------------- and getting old data");
			  output = tagCollection.aggregate(matchOne1, unwind, group, sort, limitObj);
			}
			logger.debug("query--------------"+output);
			
			logger.debug("getTagList() :: output "+output);
			MapInfo mapInfo = null;
			TagResultVO tagResultVO = null;
			for (DBObject obj : output.results()) {
			//while (tagListCursor.hasNext()) {
				  //BasicDBObject obj = (BasicDBObject) tagListCursor.next();
				mapInfo = new MapInfo();
				String tag = (String) obj.get("_id").toString();
				logger.debug("getMapTagList() :: tag "+tag);
				Gson json = new GsonBuilder().create();
				tagResultVO = json.fromJson(tag, TagResultVO.class);
				int count = (int) obj.get("count");
				mapInfo.setCount(count);
				mapInfo.setTag(tagResultVO.getTag());
				mapInfo = getTagInfoNew(tagResultVO.getTag(),mapInfo);
				mapInfoList.add(mapInfo);
				logger.debug("getTagList() :: obj "+obj);
			}
		
			return mapInfoList;
		}

		public static Date subtractDay(Date date) {

		    Calendar cal = Calendar.getInstance();
		    cal.setTime(date);
		    cal.add(Calendar.DAY_OF_MONTH,-1);
		     cal.add(Calendar.HOUR_OF_DAY, 4);
		    return cal.getTime();
		}
	/*@Override
	public ArrayList<MapInfo> getMapTagList(MapVO mapVO, int limit) throws MongoException {
		ArrayList<MapInfo> mapInfoList = new ArrayList<MapInfo>();
		double[] anArray = new double[2];
		
		anArray[0] = mapVO.getLongitude();
		anArray[1] = mapVO.getLatitude();
		//DBObject matchOne = new BasicDBObject("$geoNear", new BasicDBObject("location", anArray));
		//DBObject matchOne = new BasicDBObject("$match", new BasicDBObject("location", anArray));
		//DBObject matchTwo = new BasicDBObject("$match", new BasicDBObject("cdate", new BasicDBObject("$lt", new Date())));
		//DBObject matchThree = new BasicDBObject("$match", new BasicDBObject("country", "India"));

		DBObject unwind = new BasicDBObject("$unwind", "$tag_cloud");

		DBObject groupFields = new BasicDBObject("_id", "$tag_cloud");
		groupFields.put("count", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject sort = new BasicDBObject();
		sort.put("$sort", new BasicDBObject("count", -1));

		DBObject limitObj = new BasicDBObject();
		limitObj.put("$limit", limit);
		//manish
	
		BasicDBObject taginfo = new BasicDBObject();
		
		taginfo.put("near",anArray);
		taginfo.put("distanceField","dist.calculated");
		taginfo.put("uniqueDocs", true);
		taginfo.put("num",50);
		DBObject matchTwo2 = new BasicDBObject("type","Point");
		DBObject matchTwo4 = new BasicDBObject("coordinates",anArray);
		DBObject matchThree3 = new BasicDBObject("$maxDistance",500);
		DBObject matchOne1 = new BasicDBObject("$geoNear",taginfo);
		logger.debug("query-matchOne1-------------"+matchOne1);
		AggregationOutput output = tagCollection.aggregate(matchOne1, unwind, group, sort, limitObj);
		logger.debug("query--------------"+output);
		//DBObject query = new BasicDBObject("location",matchOne1);
		 DBCursor  tagListCursor =tagCollection.find(query); 
		 logger.debug("tagListCursor--------------"+tagListCursor);
		
				while (tagListCursor.hasNext()) {
					 BasicDBObject obj = (BasicDBObject) tagListCursor.next();
					//mapInfo = new MapInfo();
					String tag = (String) obj.get("_id").toString();
					logger.debug("tagListCursor() :: tag "+tag);
					Gson json = new GsonBuilder().create();
					tagResultVO = json.fromJson(tag, TagResultVO.class);
					int count = (int) obj.get("count");
					mapInfo.setCount(count);
					mapInfo.setTag(tagResultVO.getTag());
					mapInfo = getTagInfo(tagResultVO.getTag(),mapInfo);
					mapInfoList.add(mapInfo);
					logger.debug("tagListCursor() :: obj "+obj);
				}
		
		//AggregationOutput output = tagCollection.aggregate(matchOne, matchTwo, matchThree, unwind, group, sort, limitObj);
	         // output = tagCollection.aggregate(matchTwo, unwind, group, sort, limitObj);
		//AggregationOutput output = tagCollection.aggregate([ {$geoNear: {near: [anArray[0], anArray[1]], maxDistance: 0.008,query: { type: "public" },uniqueDocs: true, num: 5 } } ]);
		//{ location : { $near :{ $geometry : { type : "Point" ,coordinates: [ 73.1797790527344 , 35.0886516571045 ] } },$maxDistance : 500} } 
		logger.debug("getTagList() :: output "+output);
		MapInfo mapInfo = null;
		TagResultVO tagResultVO = null;
		for (DBObject obj : output.results()) {
		//while (tagListCursor.hasNext()) {
			  //BasicDBObject obj = (BasicDBObject) tagListCursor.next();
			mapInfo = new MapInfo();
			String tag = (String) obj.get("_id").toString();
			logger.debug("getMapTagList() :: tag "+tag);
			Gson json = new GsonBuilder().create();
			tagResultVO = json.fromJson(tag, TagResultVO.class);
			int count = (int) obj.get("count");
			mapInfo.setCount(count);
			mapInfo.setTag(tagResultVO.getTag());
			mapInfo = getTagInfoNew(tagResultVO.getTag(),mapInfo);
			mapInfoList.add(mapInfo);
			logger.debug("getTagList() :: obj "+obj);
		}
	
		return mapInfoList;
	}*/
	
/*	@Override
	public  ArrayList<EarthInfo> getTrendingTagList(String country, int limit) throws MongoException {
		  ArrayList<EarthInfo> earthInfoList = new ArrayList<EarthInfo>();
		double[] anArray = new double[2];
		anArray[0] = 0.0;
	    anArray[1] = 0.0;
	    DBObject matchOne = new BasicDBObject("$match", new BasicDBObject("location", anArray));
		DBObject matchTwo = new BasicDBObject("$match", new BasicDBObject("cdate",new BasicDBObject("$lt",new Date())));
	    DBObject matchThree = new BasicDBObject("$match", new BasicDBObject("country","India"));
		
		DBObject unwind = new BasicDBObject("$unwind", "$tag_cloud");
		
		DBObject groupFields = new BasicDBObject("_id", "$tag_cloud");
		groupFields.put("count", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);
		
		DBObject sort = new BasicDBObject();
		sort.put("$sort", new BasicDBObject("count", -1));		
		
		DBObject limitObj = new BasicDBObject();
		limitObj.put("$limit", limit);	
		AggregationOutput output = tagCollection.aggregate(matchOne,matchTwo,matchThree,unwind,group,sort,limitObj);
		EarthInfo earthInfo = null;
		TagResultVO tagResultVO = null;
		for (DBObject obj : output.results()) {
			 earthInfo = new EarthInfo();
		     String tag = (String) obj.get("_id").toString();
		     Gson json =  new GsonBuilder().create();
		     tagResultVO = json.fromJson(tag,TagResultVO.class);
		     int count = (int) obj.get("count");
		     earthInfo.setCountry("India");
		     earthInfo.setTagName(tagResultVO.getTag());
		     earthInfo.setContCnt((long)count);
		     earthInfoList.add(earthInfo);
		}
		return earthInfoList;
	}
*/
}
