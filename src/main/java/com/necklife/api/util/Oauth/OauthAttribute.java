package com.necklife.api.util.Oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necklife.api.web.exception.JsonParsingException;
import lombok.Getter;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.json.JSONObject;

import java.util.Map;

public class OauthAttribute {

    private static final  JSONParser parser = new JSONParser();
    private Map<String, Object> attributes;

    public OauthAttribute(String token) {
        attributes = getStringObjectMap(token);

    }

    private Map<String, Object> getStringObjectMap(String token) {
        final Map<String, Object> attributes = null;
        JSONObject jsonBody = null;
        try {
            jsonBody = (JSONObject) parser.parse(token);
            this.attributes= new ObjectMapper().readValue(jsonBody.toString(), Map.class);
            
        } catch (ParseException | JsonProcessingException e) {
            throw new JsonParsingException(String.valueOf(e));
        }
        return attributes;
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }



}
