package com.voizd.dao.mongo.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.mongodb.MongoException;
import com.voizd.common.beans.vo.MapVO;
import com.voizd.common.beans.vo.TagCloudVO;
import com.voizd.common.beans.vo.TapClipVO;
import com.voizd.dao.entities.EarthInfo;
import com.voizd.dao.entities.MapInfo;


public interface MongoService {
	public void insertVStream(TapClipVO tapClipVO,Long userId)throws MongoException;
	public HashMap<String, Object> getUserVStream(Long userId,String type,int slice)throws MongoException;
	public HashMap<String, Object> getMyVStream(Long userId,String type,int skip)throws MongoException;
	public HashMap<String, Object> getIFollowVStream(Long userId,int slice)throws MongoException;
	public void updateFollowerVStream(TapClipVO tapClipVO,Long userId)throws MongoException;
	public void upsertIFollowerVStream(TapClipVO tapClipVO,Long userId)throws MongoException;
	public void deleteContent(TapClipVO tapClipVO)throws MongoException;
	//public void deleteContentFromIFollow(TapClipVO tapClipVO,Long userId)throws MongoException;
	public void deleteContentFromVStream(TapClipVO tapClipVO,Long userId)throws MongoException;
//	public void deleteFromIFollowerVStream(TapClipVO tapClipVO,Long userId)throws MongoException;
	//public void deleteFromIUserVStream(TapClipVO tapClipVO,Long userId)throws MongoException;
	public void deleteFromMyStream(TapClipVO tapClipVO,Long userId)throws MongoException;
	public void updateClipInfo(TapClipVO tapClipVO)throws MongoException;
	public void insertTagDetail(TagCloudVO tagCloudVO)throws MongoException;
	public  ArrayList<EarthInfo> getTrendingTagList(String country,int limit)throws MongoException;
	public void upsertAmplifyVStream(TapClipVO tapClipVO,Long userId) throws MongoException;
	public void upsertMyStreamClip(TapClipVO tapClipVO) throws MongoException;
	public void upsertAmplifyMyStreamClip(TapClipVO tapClipVO) throws MongoException;
	public void deAmplifyClip(TapClipVO tapClipVO,Long userId) throws MongoException;	
	public  ArrayList<MapInfo> getTagList(String country,int limit)throws MongoException;
	public  ArrayList<MapInfo> getMapTagList(MapVO mapVO,int limit)throws MongoException;
}
