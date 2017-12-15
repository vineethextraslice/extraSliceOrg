package com.app.extraslice.model;

import com.app.extraslice.utils.CustomException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StripePlanModel {
	
	String stripePlanId;
	int eslicePlanId;
	String stripePlanName;
	String resIds;
	int id;

	public int getEslicePlanId() {
		return eslicePlanId;
	}

	public void setEslicePlanId(int eslicePlanId) {
		this.eslicePlanId = eslicePlanId;
	}
	public String getStripePlanId() {
		return stripePlanId;
	}

	public void setStripePlanId(String stripePlanId) {
		this.stripePlanId = stripePlanId;
	}

	

	public String getStripePlanName() {
		return stripePlanName;
	}

	public void setStripePlanName(String stripePlanName) {
		this.stripePlanName = stripePlanName;
	}

	public String getResIds() {
		return resIds;
	}

	public void setResIds(String resIds) {
		this.resIds = resIds;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public StripePlanModel jSonToObject(String jsonString) throws CustomException {
		StripePlanModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, StripePlanModel.class);
		} catch (JsonParseException e) {
			throw new CustomException("Error while parsing input string : " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			throw new CustomException("Error while  parsing input string : " + e.getLocalizedMessage());
		} catch (IOException e) {
			throw new CustomException("Error while  parsing input string : " + e.getLocalizedMessage());
		}
		return uModel;
	}

}
