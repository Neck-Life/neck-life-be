package com.necklife.api.web.client.unlink.dto;

import lombok.Data;

@Data
public class KaKaoUnlinkData implements SocialUnlinkData {
	private String id;

	@Override
	public String getUnlinkInfo() {
		return id;
	}
}
