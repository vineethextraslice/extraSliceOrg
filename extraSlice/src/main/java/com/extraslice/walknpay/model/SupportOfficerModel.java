package com.extraslice.walknpay.model;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SupportOfficerModel {
	String officerUserName;;
	boolean active;
	boolean available;
	int officerId;
	String chatId;
	int userId;
	int storeId;
	String chatServerIP = "";
	int chatServerPort;
	String chatServerService = "";
	int currSupportCnt = 0;
	public String getOfficerUserName() {
		return officerUserName;
	}

	public void setOfficerUserName(String officerUserName) {
		this.officerUserName = officerUserName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getOfficerId() {
		return officerId;
	}

	public void setOfficerId(int officerId) {
		this.officerId = officerId;
	}

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public String getChatServerIP() {
		return chatServerIP;
	}

	public void setChatServerIP(String chatServerIP) {
		this.chatServerIP = chatServerIP;
	}

	public int getChatServerPort() {
		return chatServerPort;
	}

	public void setChatServerPort(int chatServerPort) {
		this.chatServerPort = chatServerPort;
	}

	public String getChatServerService() {
		return chatServerService;
	}

	public int getCurrSupportCnt() {
		return currSupportCnt;
	}

	public void setCurrSupportCnt(int currSupportCnt) {
		this.currSupportCnt = currSupportCnt;
	}

	public void setChatServerService(String chatServerService) {
		this.chatServerService = chatServerService;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public SupportOfficerModel jSonToObject(String jsonString) {
		SupportOfficerModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, SupportOfficerModel.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uModel;
	}
}
