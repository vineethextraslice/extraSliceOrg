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
public class AdminAccountModel {
	String strpSecKey;
	String paypalClientId;
	String paypalEnv;
	String about;
	String contactNo;
	String contactEmail;
	String termsNCondUrl;
	String privacyPolicyUrl;
	String webserviceVersion;

	public String getStrpSecKey() {
		return strpSecKey;
	}

	public void setStrpSecKey(String strpSecKey) {
		this.strpSecKey = strpSecKey;
	}

	public String getPaypalClientId() {
		return paypalClientId;
	}

	public void setPaypalClientId(String paypalClientId) {
		this.paypalClientId = paypalClientId;
	}

	public String getPaypalEnv() {
		return paypalEnv;
	}

	public void setPaypalEnv(String paypalEnv) {
		this.paypalEnv = paypalEnv;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	
	public String getTermsNCondUrl() {
		return termsNCondUrl;
	}

	public void setTermsNCondUrl(String termsNCondUrl) {
		this.termsNCondUrl = termsNCondUrl;
	}

	
	public String getPrivacyPolicyUrl() {
		return privacyPolicyUrl;
	}

	public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
		this.privacyPolicyUrl = privacyPolicyUrl;
	}

	public String getWebserviceVersion() {
		return webserviceVersion;
	}

	public void setWebserviceVersion(String webserviceVersion) {
		this.webserviceVersion = webserviceVersion;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public AdminAccountModel jSonToObject(String jsonString) throws CustomException {
		AdminAccountModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, AdminAccountModel.class);
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
