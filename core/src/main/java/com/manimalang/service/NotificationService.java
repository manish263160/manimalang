package com.manimalang.service;

import java.util.List;

import com.manimalang.exception.GenericException;
import com.manimalang.models.NotificationDetails;

public interface NotificationService {

	public boolean pushNotificationToGCM(String gcmRegId,String message);

	public boolean sendNotification(NotificationDetails notificationDetails) throws GenericException;
	
	public List<String> getAllDeviceId();

	public boolean insertDevice(String deviceId);

	public boolean pushNotificationCron();

	public boolean checkVideoLink(String urllink, String from);
}
