package com.necklife.api.web.client.member;

import com.necklife.api.web.client.member.dto.SocialMemberToken;

@FunctionalInterface
public interface SocialMemberClient {
    /**
     * Get social member data by userId
     * @param targetId social user id
     */
    SocialMemberToken execute(String targetId);
}