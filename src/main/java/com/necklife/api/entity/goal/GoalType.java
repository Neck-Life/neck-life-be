package com.necklife.api.entity.goal;

public enum GoalType {
	MEASUREMENT("MEASUREMENT"),
	SCORE("SCORE"),

	FORWARD("FORWARD"),
	BACKWARD("BACKWARD");

	private final String type;

	GoalType(String type) {
		this.type = type;
	}
}
