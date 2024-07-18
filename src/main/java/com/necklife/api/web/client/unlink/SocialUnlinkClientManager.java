package com.necklife.api.web.client.unlink;

import com.necklife.api.web.client.unlink.dto.SocialUnlinkData;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SocialUnlinkClientManager {

	private final List<SocialUnlinkClient> socialUnlinkClients;

	public SocialUnlinkClientManager(List<SocialUnlinkClient> socialUnlinkClients) {
		this.socialUnlinkClients = socialUnlinkClients;
	}

	public SocialUnlinkData execute(String socialType, String targetId) {
		for (SocialUnlinkClient client : socialUnlinkClients) {
			if (client.supports(socialType)) {
				return client.execute(targetId);
			}
		}
		throw new IllegalArgumentException("Unsupported social type: " + socialType);
	}
}
