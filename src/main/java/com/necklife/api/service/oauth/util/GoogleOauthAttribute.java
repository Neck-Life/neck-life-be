package com.necklife.api.service.oauth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necklife.api.web.exception.JsonParsingException;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GoogleOauthAttribute implements OauthAttribute {

	private static final JSONParser parser = new JSONParser();
	private Map<String, Object> attributes;

	public GoogleOauthAttribute(String token) {
		attributes = getStringObjectMap(token);
	}

	private Map<String, Object> getStringObjectMap(String token) {
		Map<String, Object> attributes = null;
		JSONObject jsonBody = null;
		try {
			jsonBody = (JSONObject) parser.parse(token);
			attributes = new ObjectMapper().readValue(jsonBody.toString(), Map.class);

		} catch (ParseException | JsonProcessingException e) {
			throw new JsonParsingException(String.valueOf(e));
		}
		return attributes;
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}
}
