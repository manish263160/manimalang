package com.manimalang.dao;

import java.util.List;

import com.manimalang.models.NotificationDetails;

public interface NotiFicationDao {

	boolean sendNotification(NotificationDetails notificationDetails);

	List<String> getAllDeviceId();

	boolean insertDevice(String deviceId);

	List<NotificationDetails> getAllScheduleTask(String currentTime);

	boolean checkVideoLink(String urllink, String from);

}
