package com.voizd.dao.mongo.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.voizd.common.beans.vo.AmplifyInfoVO;
import com.voizd.common.beans.vo.TagCloudVO;
import com.voizd.common.beans.vo.TapClipVO;

public  class MongoHelper {
	private static final Logger logger = LoggerFactory.getLogger(MongoHelper.class);
	
	public static DBObject bsonFromPojoForClip(TapClipVO tapClipVO) {
		BasicDBObject document = new BasicDBObject();
		document.put("contentid", tapClipVO.getContentId());
		document.put("cfileid",tapClipVO.getFileIds());
		document.put("language", tapClipVO.getLanguage());
		document.put("title", tapClipVO.getTitle());
		document.put("date", tapClipVO.getCreatedDate());
		document.put("stationid", tapClipVO.getStationId());
		document.put("status",tapClipVO.getStatus());
		document.put("city", tapClipVO.getCity());
		document.put("state", tapClipVO.getState());
		document.put("country", tapClipVO.getCountry());
		document.put("location",tapClipVO.getLocation());
		document.put("latitude", tapClipVO.getLatitude());
		document.put("longitude", tapClipVO.getLongitude());
		document.put("address", tapClipVO.getAddress());
		document.put("ufileid", tapClipVO.getUserFileId());
		document.put("creatorid", tapClipVO.getCreatorId());
		document.put("weblink", tapClipVO.getWeblink());
		return document;
	}

	public static DBObject bsonFromPojoForMyClip(TapClipVO tapClipVO) {
		BasicDBObject document = new BasicDBObject();
		document.put("contentid", tapClipVO.getContentId());
		document.put("amplify",tapClipVO.isAmplify());
		return document;
	}
	
	public static DBObject bsonFromPojoForMyAmplifyClip(TapClipVO tapClipVO) {
		BasicDBObject document = new BasicDBObject();
		document.put("contentid", tapClipVO.getContentId());
		document.put("amplify",tapClipVO.isAmplify());
		DBObject dbo=null;
		ArrayList<DBObject> array = new ArrayList<DBObject>();
		dbo = bsonFromAmplifyByUserId(tapClipVO.getAmplifierId());
		array.add(dbo);
		document.put("mp",array);	
		return document;
	}
	
	public static DBObject bsonFromPojoForTap(TapClipVO tapClipVO) {
		BasicDBObject document = new BasicDBObject();
		document.put("contentid", tapClipVO.getContentId());
		document.put("creatorid", tapClipVO.getCreatorId());
		document.put("status",tapClipVO.getStatus());
		document.put("date", tapClipVO.getCreatedDate());
		//document.put("date", tapClipVO.getCreatedDate());	
		/*ArrayList<Long> amplifierArray = bsonFromPojoForAmplify(tapClipVO);
		if(null!=amplifierArray && amplifierArray.size()>0){
			document.put("amplifier", bsonFromPojoForAmplify(tapClipVO));
		}*/
		return document;
	}
	
	public static DBObject bsonFromPojoForAmplify(TapClipVO tapClipVO) {
		BasicDBObject document = new BasicDBObject();
		document.put("contentid", tapClipVO.getContentId());
		document.put("creatorid", tapClipVO.getCreatorId());
		document.put("status",tapClipVO.getStatus());
		document.put("date", tapClipVO.getCreatedDate());
		document.put("amplify",tapClipVO.isAmplify());	
		DBObject dbo=null;
		ArrayList<DBObject> array = new ArrayList<DBObject>();
		dbo = bsonFromAmplifyByUserId(tapClipVO.getAmplifierId());
		array.add(dbo);
		document.put("mp",array);	
		
		return document;
	}
	
	public static DBObject bsonFromPojoFormAmplify(DBObject clipInfo,TapClipVO tapClipVO) {
	//	BasicDBObject document = new BasicDBObject();
	//	document.put("contentid", tapClipVO.getContentId());
	//	document.put("creatorid", tapClipVO.getCreatorId());
	//	document.put("status",tapClipVO.getStatus());
	//	document.put("date", tapClipVO.getCreatedDate());
		clipInfo.put("clipinfo.$.amplify",tapClipVO.isAmplify());	
		DBObject dbo=null;
		ArrayList<DBObject> array = new ArrayList<DBObject>();
		dbo = bsonFromAmplifyByUserId(tapClipVO.getAmplifierId());
		array.add(dbo);
		clipInfo.put("clipinfo.$.mp",array);			
		return clipInfo;
	}
	
	public static ArrayList<Long>  bsonFromPojoForAmplify(AmplifyInfoVO tapClipVO) {
		ArrayList<Long> array = new ArrayList<Long>();
		array.add(tapClipVO.getAmplifierId());
		return array;
	}
	
	public static ClipInfo makeClipInfoPojoFromBson(BasicDBObject b) {
		ClipInfo clipInfo = new ClipInfo();
		clipInfo.setContentId((Long) b.get("contentid"));
		BasicDBList dbo = (BasicDBList) (b.get("mp"));
	
	   if(dbo != null) {
			boolean isAmplify = (Boolean) b.get("amplify");
			clipInfo.setAmplify(isAmplify);
			for (Iterator<Object> it = dbo.iterator(); it.hasNext();) {
				BasicDBObject dboAmplify = (BasicDBObject) it.next();
				Long amplifierId =(Long) dboAmplify.get("ampbyid");
				clipInfo.setAmplifierId(amplifierId);
				break;
			}
		}
		return clipInfo;
	}
	
	public static ClipInfo makeClipInfoPojoFromBsonMyStream(BasicDBObject b) {
		ClipInfo clipInfo = new ClipInfo();
		clipInfo.setContentId((Long) b.get("contentid"));

		// if (isAmplify) {
		BasicDBList dbo = (BasicDBList) (b.get("mp"));
		if (dbo != null) {
			for (Iterator<Object> it = dbo.iterator(); it.hasNext();) {
				BasicDBObject dboAmplify = (BasicDBObject) it.next();
				Long amplifierId = (Long) dboAmplify.get("ampbyid");
				logger.debug("MongoDB makeClipInfoPojoFromBsonMyStream :: amplifierId :: " + amplifierId);
				clipInfo.setAmplifierId(amplifierId);
				break;
			}
			if (clipInfo != null && clipInfo.getAmplifierId() != null) {
				boolean isAmplify = (Boolean) b.get("amplify");
				logger.debug("MongoDB makeClipInfoPojoFromBsonMyStream :: isAmplify :: " + isAmplify);
				clipInfo.setAmplify(isAmplify);
			}
		}
		// }
		return clipInfo;
	}

	public static TapClipVO makeClipDetailsPojoFromBson(BasicDBObject b) {
		TapClipVO tapClipVO = new TapClipVO();
		tapClipVO.setContentId((Long) b.get("contentid"));
		tapClipVO.setCreatorId((Long) b.get("creatorid"));
		tapClipVO.setCreatedDate((Date) b.get("date"));
		int status = (int)b.get("status");
		tapClipVO.setStatus((byte)status);
		tapClipVO.setAddress((String) b.get("address"));
		tapClipVO.setCity((String)b.get("city"));
		tapClipVO.setCountry((String)b.get("country"));
		tapClipVO.setState((String)b.get("state"));
		if(b.get("location") != null){
		tapClipVO.setLocation((String)b.get("location"));
		}
		tapClipVO.setWeblink((String)b.get("weblink"));
		//tapClipVO.setLatitude((Float)b.get("latitude"));
		//tapClipVO.setLongitude((Float)b.get("longitude"));
		tapClipVO.setLanguage((String)b.get("language"));
		tapClipVO.setStationId((Long)b.get("stationid"));
		tapClipVO.setFileIds((String)b.get("cfileid"));
		tapClipVO.setUserFileId((String)b.get("ufileid"));
		tapClipVO.setTitle((String)b.get("title"));
	 return tapClipVO;
	}
	
	public static DBObject bsonFromPojo(TagCloudVO tagCloudVO) {
		BasicDBObject document = new BasicDBObject();
		DBObject dbo;
		ArrayList<DBObject> array = new ArrayList<DBObject>();
		dbo = bsonFromPojoForTag(tagCloudVO.getLatitude(),tagCloudVO.getLongitude());
		array.add(dbo);
		document.put("location", array);
		document.put("contentid", tagCloudVO.getContentId());
		if (tagCloudVO.getTagCloud() != null) {
		 document.put("tag_cloud", bsonFromPojoForTagArray(tagCloudVO.getTagCloud()));
		}
		document.put("cdate", tagCloudVO.getCreatedDate());
		document.put("creatorid", tagCloudVO.getCreatorId());
		document.put("stationid", tagCloudVO.getStationId());
		document.put("country", tagCloudVO.getCountry());
		document.put("language", tagCloudVO.getLanguage());
		
		return document;
	}
	public static DBObject bsonFromPojoForTag(double latitude,double longitude) {
		BasicDBObject document = new BasicDBObject();
		document.put("latitude",latitude);
		document.put("longitude",longitude);
		return document;
	}
	
	public static ArrayList<DBObject> bsonFromPojoForTagArray(String tags) {
		ArrayList<DBObject> array = new ArrayList<DBObject>();
		String tagList [] = tags.split(",");
		BasicDBObject document =null;
		for(String tag : tagList){
			document = new BasicDBObject();
		    document.put("tag",tag);
		    array.add(document);
		}
		return array;
	}
	public static DBObject bsonFromAmplifyByUserId(long amplifyByUserId) {
		BasicDBObject document = new BasicDBObject();
		document.put("ampbyid",amplifyByUserId);
		return document;
	}
}
