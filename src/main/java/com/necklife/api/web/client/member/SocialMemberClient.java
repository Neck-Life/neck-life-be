package com.necklife.api.web.client.member;

import java.text.ParseException;

@FunctionalInterface
public interface SocialMemberClient {
	/**
	 * Get social member data by userId
	 *
	 * @param targetId social user id
	 */
	String execute(String targetId) throws ParseException;
}
