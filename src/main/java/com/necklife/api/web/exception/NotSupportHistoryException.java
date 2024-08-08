package com.necklife.api.web.exception;

public class NotSupportHistoryException extends IllegalArgumentException {

    public NotSupportHistoryException() {
        super("지원하지 않는 히스토리 입니다.");
    }
}
