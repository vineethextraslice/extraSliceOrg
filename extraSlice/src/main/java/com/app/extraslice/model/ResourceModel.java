package com.app.extraslice.model;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import com.app.extraslice.utils.CustomException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceModel {
	String resourceName;
	int resourceId;
	String resourceType;
	boolean isFree;
	int maxBookingDuration;
	int minSlotDuration;
	String imageUrl;
	double resourcePrice;
	String 	resourceDesc;
	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	
	public int getMaxBookingDuration() {
		return maxBookingDuration;
	}

	public void setMaxBookingDuration(int maxBookingDuration) {
		this.maxBookingDuration = maxBookingDuration;
	}

	public int getMinSlotDuration() {
		return minSlotDuration;
	}

	public void setMinSlotDuration(int minSlotDuration) {
		this.minSlotDuration = minSlotDuration;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
	public double getResourcePrice() {
		return resourcePrice;
	}

	
	public String getResourceDesc() {
		return resourceDesc;
	}

	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}

	public void setResourcePrice(double resourcePrice) {
		this.resourcePrice = resourcePrice;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public ResourceModel jSonToObject(String jsonString) throws CustomException {
		ResourceModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, ResourceModel.class);
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
