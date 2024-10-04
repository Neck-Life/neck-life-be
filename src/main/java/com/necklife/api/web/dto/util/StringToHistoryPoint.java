package com.necklife.api.web.dto.util;

import com.necklife.api.web.dto.request.history.HistoryPointEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToHistoryPoint implements Converter<String, HistoryPointEnum> {

	@Override
	public HistoryPointEnum convert(String source) {
		return HistoryPointEnum.valueOf(source.toUpperCase());
	}
}
