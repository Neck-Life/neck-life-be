package com.necklife.api.web.dto.request.history;

import org.webjars.NotFoundException;

public enum HistoryPointEnum {
	WEEK,
	MONTH1,
	MONTH3,
	MONTH6;

	public Integer covertToDateBefore() {
		if (this.name().equals(WEEK.name())) {
			return 7;
		} else if (this.name().equals(MONTH1.name())) {
			return 30;
		} else if (this.name().equals(MONTH3.name())) {
			return 90;
		} else if (this.name().equals(MONTH6.name())) {
			return 180;
		} else {
			throw new NotFoundException("point 기간을 다시 설정 해주세요");
		}
	}
}
