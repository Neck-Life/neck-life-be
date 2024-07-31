package com.necklife.api.entity.history;

public enum PoseStatus {

    FORWARD("FORWARD"),
    BACKWARD("BACKWARD"),
    TILTED("TILTED"),
    NORMAL("NORMAL");

    private final String status;


    PoseStatus(String status) {
        this.status = status;
    }
}
