package com.voizd.dao.mongo.service;

import java.util.List;

import com.mongodb.MongoException;
import com.voizd.common.beans.vo.Notification;

public interface NotificationService {
	public void insertBulkNotification(Long userId,List<Notification> notifications)throws MongoException;
	public void insertNotification(Long userId,Notification notification)throws MongoException;
	public void upsertNotification(Long userId,Notification notification)throws MongoException;
	public List<Notification> getUserNotificationById(Long userId,String id,int limit)throws MongoException;
}
