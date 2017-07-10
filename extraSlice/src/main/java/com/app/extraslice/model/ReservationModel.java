package com.app.extraslice.model;

import java.io.IOException;
import java.util.Date;

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
public class ReservationModel {
	int reservedByUser;
	int reservedByOrg;
	String reservedByOrgName;
	Date startDate;
	Date endTime;
	int duration;
	String resourceType;
	int resourceId;
	String resourceName;
	int smSpaceId;
	String smSpaceName;
	int reservationId;
	int resourceTypeId;
	String reservedByUserName;
	String reservationName;
	String description;

	

	public int getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(int resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	public String getReservedByUserName() {
		return reservedByUserName;
	}

	public void setReservedByUserName(String reservedByUserName) {
		this.reservedByUserName = reservedByUserName;
	}

	public int getReservedByUser() {
		return reservedByUser;
	}

	public void setReservedByUser(int reservedByUser) {
		this.reservedByUser = reservedByUser;
	}

	public int getReservedByOrg() {
		return reservedByOrg;
	}

	public void setReservedByOrg(int reservedByOrg) {
		this.reservedByOrg = reservedByOrg;
	}

	public String getReservedByOrgName() {
		return reservedByOrgName;
	}

	public void setReservedByOrgName(String reservedByOrgName) {
		this.reservedByOrgName = reservedByOrgName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public int getSmSpaceId() {
		return smSpaceId;
	}

	public void setSmSpaceId(int smSpaceId) {
		this.smSpaceId = smSpaceId;
	}

	public String getSmSpaceName() {
		return smSpaceName;
	}

	public void setSmSpaceName(String smSpaceName) {
		this.smSpaceName = smSpaceName;
	}

	public String getReservationName() {
		return reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public ReservationModel jSonToObject(String jsonString) throws CustomException {
		ReservationModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, ReservationModel.class);
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
