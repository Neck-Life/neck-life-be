package com.necklife.api.notification.service;

import com.google.firebase.messaging.*;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.member.MemberRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostureNotificationService {

	private final MemberRepository memberRepository;
	private final FCMService fcmService;

	// 한국 시간대 사용자에게 알림 전송
	@Scheduled(cron = "0 * * * * ?", zone = "Asia/Seoul")
	public void sendKoreaTimezoneNotifications() {
		List<MemberEntity> members = memberRepository.findAllByTimeZone("Asia/Seoul");
		List<String> fcmTokens =
				members.stream()
						.map(MemberEntity::getNotificationToken)
						.filter(Objects::nonNull)
						.collect(Collectors.toList());

		sendMulticastNotification(fcmTokens, "지금! 거북목 아니신가요?", "바른 자세를 유지할 시간이에요!");
	}

	// 미국 뉴욕 시간대 사용자에게 알림 전송
	@Scheduled(cron = "0 0 14 * * ?", zone = "America/New_York")
	public void sendUSTimezoneNotifications() {
		List<MemberEntity> members = memberRepository.findAllByAmericanTimeZones();
		List<String> fcmTokens =
				members.stream()
						.map(MemberEntity::getNotificationToken)
						.filter(Objects::nonNull)
						.collect(Collectors.toList());

		sendMulticastNotification(fcmTokens, "Check Your Posture!", "Time to check your posture!");
	}

	// 멀티캐스트 알림 전송 메서드
	private void sendMulticastNotification(List<String> fcmTokens, String title, String body) {
		if (fcmTokens.isEmpty()) return;

		MulticastMessage message =
				MulticastMessage.builder()
						.addAllTokens(fcmTokens)
						.setNotification(Notification.builder().setTitle(title).setBody(body).build())
						.build();

		try {
			BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
			System.out.println(response.getSuccessCount() + " messages were sent successfully");
		} catch (FirebaseMessagingException e) {
			System.err.println("Error sending FCM messages: " + e.getMessage());
		}
	}
}
