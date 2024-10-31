package com.necklife.api.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FCMInitializer {

	@Value("${firebase.key-path}")
	String fcmKeyPath;

	@PostConstruct
	public void getFcmCredential() {
		try (InputStream refreshToken = new FileInputStream(fcmKeyPath)) { // FileInputStream으로 절대 경로 파일 읽기
			FirebaseOptions options =
					FirebaseOptions.builder()
							.setCredentials(GoogleCredentials.fromStream(refreshToken))
							.build();

			FirebaseApp.initializeApp(options);
			log.info("Fcm Setting Completed");
		} catch (IOException e) {
			throw new RuntimeException("Failed to initialize FCM", e);
		}
	}
}
