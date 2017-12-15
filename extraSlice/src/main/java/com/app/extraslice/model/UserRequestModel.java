package com.app.extraslice.model;

import com.app.extraslice.utils.CustomException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequestModel {

	String name;
	String email;
	int userId;
	int noOfSeats;
	String contactNo;
	String planName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getNoOfSeats() {
		return noOfSeats;
	}

	public void setNoOfSeats(int noOfSeats) {
		this.noOfSeats = noOfSeats;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, false);
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public UserRequestModel jSonToObject(String jsonString) throws CustomException {
		UserRequestModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, UserRequestModel.class);
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
