package com.app.extraslice.model;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import com.app.extraslice.utils.CustomException;

public class SmartSpaceModel {
	int smSpaceId;
	String smSapceName;
	String address;
	double latitude;
	double longitude;
	List<ResourceModel> resourceList;
	
	
	public int getSmSpaceId() {
		return smSpaceId;
	}

	public void setSmSpaceId(int smSpaceId) {
		this.smSpaceId = smSpaceId;
	}

	public String getSmSapceName() {
		return smSapceName;
	}

	public void setSmSapceName(String smSapceName) {
		this.smSapceName = smSapceName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public List<ResourceModel> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<ResourceModel> resourceList) {
		this.resourceList = resourceList;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public SmartSpaceModel jSonToObject(String jsonString) throws CustomException {
		SmartSpaceModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, SmartSpaceModel.class);
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
