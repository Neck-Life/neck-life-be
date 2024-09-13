package com.necklife.api.web.client.mail;

import static org.junit.jupiter.api.Assertions.*;

import com.necklife.api.ApiApplication;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles(value = "local")
public class MailClientTest {

	private MailClient mailClient = new MailClient();

	@Test
	public void testMail() {
		mailClient.sendEmail("ccnoi1532@naver.com", "123", "123");
	}
}
