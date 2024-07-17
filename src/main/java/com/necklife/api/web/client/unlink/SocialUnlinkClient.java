package com.necklife.api.web.client.unlink;

import com.necklife.api.web.client.unlink.dto.SocialUnlinkData;

public interface SocialUnlinkClient {

    /**
     * Unlink social account by targetId
     * @param targetId social user id
     */
    SocialUnlinkData execute(String targetId);

    boolean supports(String type);
}