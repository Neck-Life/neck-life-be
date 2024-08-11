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

	public String getDescription(double targetValue) {
		switch (this) {
			case MEASUREMENT:
				return String.format("하루에 %.1f시간 이상 측정하기", targetValue);
			case SCORE:
				return String.format("하루에 %.1f점 이상 획득하기", targetValue);
			default:
				return "";
		}
	}
}
