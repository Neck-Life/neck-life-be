package com.necklife.api.entity.history;

public enum PoseStatus {
	FORWARD("FORWARD"),
	BACKWARD("BACKWARD"),
	TILT("TILT"),
	DOWNNORMAL("DOWNNORMAL"),

	// deprecated
	NORMAL("NORMAL"),
	FORWARDNORMAL("FORWARDNORMAL"),
	TILTNORMAL("TILTNORMAL"),
	DOWN("DOWN"),
	UNKNOWN("UNKNOWN"),

	START("START"),
	END("END");

	private final String status;

	PoseStatus(String status) {
		this.status = status;
	}
}
