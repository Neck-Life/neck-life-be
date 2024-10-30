package com.necklife.api.notification.service;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

	public void sendNotification(String userId, String title, String messageBody) {
		Message message =
				Message.builder()
						.setToken(userId)
						.setNotification(Notification.builder().setTitle("NeckLife").setBody("test").build())
						.setApnsConfig(
								ApnsConfig.builder().setAps(Aps.builder().setSound("default").build()).build())
						.build();

		FirebaseMessaging.getInstance().sendAsync(message);
	}
}
