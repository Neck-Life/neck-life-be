package com.necklife.api.service.inquiry;

import com.necklife.api.entity.member.InquiryEntity;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.member.InquiryRepository;
import com.necklife.api.repository.member.MemberRepository;
import com.slack.api.Slack;
import com.slack.api.webhook.WebhookResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostInquiryService {
	@Value("${slack.webhook-url}")
	private String webhookUrl;

	private final InquiryRepository inquiryRepository;
	private final MemberRepository memberRepository;

	//    private final MailClient mailClient;

	public void execute(String memberId, String title, String content) {
		Optional<MemberEntity> findMember = memberRepository.findById(memberId);
		if (findMember.isEmpty()) {
			throw new IllegalArgumentException("Member not found");
		}
		//        mailClient.sendEmail("ccnoi1532@naver.com",title,content);
		sendToSlack(findMember.get().getEmail(), title, content);
		InquiryEntity inquiry =
				InquiryEntity.builder().member(findMember.get()).title(title).content(content).build();
		inquiryRepository.save(inquiry);
	}

	private void sendToSlack(String senderEmail, String title, String content) {
		// Slack 메시지 텍스트 구성
		String text =
				String.format(
						"📢 *New Inquiry Received*\n"
								+ "*Sender Email:* %s\n"
								+ "*Reply To Email:* %s\n"
								+ "*Message Content:* %s",
						senderEmail, title, content);
		Slack slack = Slack.getInstance();
		// Slack Webhook Payload 생성
		String payload = "{ \"text\": \"" + text + "\" }";

		try {
			WebhookResponse response = slack.send(webhookUrl, payload);
			System.out.println(response);
		} catch (IOException e) {
			//			log.error("slack 메시지 발송 중 문제가 발생했습니다.", e.toString());
			throw new RuntimeException(e);
		}
	}
}
