package com.necklife.api.web.client.exception;

public class SocialClientException extends RuntimeException {
    public SocialClientException() {
        super("클라이언트 에러가 발생하였습니다.");
    }

    public SocialClientException(String message) {
        super(message);
    }
}