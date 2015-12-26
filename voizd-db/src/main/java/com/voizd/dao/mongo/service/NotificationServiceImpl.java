package com.voizd.dao.mongo.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.voizd.common.beans.vo.MongoServer;
import com.voizd.common.beans.vo.Notification;

public class NotificationServiceImpl implements NotificationService {
	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
	private volatile static NotificationServiceImpl notificationServiceImpl;
	private DBCollection notifcationCollection = null;
    private static String SEPARATOR =":"; 
    
	public static NotificationServiceImpl getInstance() throws  UnknownHostException, MongoException {
		if (notificationServiceImpl == null) {
			synchronized (NotificationServiceImpl.class) {
				if (notificationServiceImpl == null) {
					notificationServiceImpl = new NotificationServiceImpl();
				}
			}
		}
		return notificationServiceImpl;
	}
	
	private NotificationServiceImpl(){
		setUpUserTapInfo();
	}
	
	@Override
	public void insertBulkNotification(Long userId,
			List<Notification> notifications) throws MongoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertNotification(Long userId, Notification notification)
			throws MongoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void upsertNotification(Long userId, Notification notification)
			throws MongoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Notification> getUserNotificationById(Long userId, String id,
			int limit) throws MongoException {
		// TODO Auto-generated method stub
		return null;
	}
	private void setUpUserTapInfo() throws  MongoException {
		ArrayList<ServerAddress> serverList = getServerList();
		MongoClient mongoClient = new MongoClient(serverList,new MongoClientOptions.Builder().build());
		mongoClient.setWriteConcern(WriteConcern.SAFE);
		mongoClient.setWriteConcern(WriteConcern.UNACKNOWLEDGED);
		DB db = mongoClient.getDB("voizdNotfidb");
		notifcationCollection = db.getCollection("notification");
		if(notifcationCollection == null){
			logger.error("Error while getting notifcationCollection "+notifcationCollection);
			throw new RuntimeException("MongoDB connection not found .Please check config ");
		}
    }
	private ArrayList<ServerAddress> getServerList(){
		ArrayList<ServerAddress> serverList = new ArrayList<ServerAddress>();
		
		try{
			List<MongoServer>  mongoServers = null;//mongoDAO.getMongoServers();
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
}
