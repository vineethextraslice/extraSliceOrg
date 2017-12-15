package com.extraslice.walknpay.model;

import com.extraslice.walknpay.bl.CustomException;

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
public class UserModel {
	String userName;
	String firstName;
	String lastName;
	String email;
	String password;
	int userId;
	int roleId;
	String roleName;
	String tempPassword;
	String verificationCode;
	boolean usingTempPwd;
	boolean autoEmail;
	String addressLine1;
	String addressLine2;
	String addressLine3;
	String zip;
	String state;
	double userLatitude;
	double userLongitude;
	String authCode;
	
	
	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public double getUserLatitude() {
		return userLatitude;
	}

	public void setUserLatitude(double userLatitude) {
		this.userLatitude = userLatitude;
	}

	public double getUserLongitude() {
		return userLongitude;
	}

	public void setUserLongitude(double userLongitude) {
		this.userLongitude = userLongitude;
	}

	

	public boolean getUsingTempPwd() {
		return usingTempPwd;
	}

	public void setUsingTempPwd(boolean usingTempPwd) {
		this.usingTempPwd = usingTempPwd;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}


	public String getTempPassword() {
		return tempPassword;
	}

	public void setTempPassword(String tempPassword) {
		this.tempPassword = tempPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isAutoEmail() {
		return autoEmail;
	}

	public void setAutoEmail(boolean autoEmail) {
		this.autoEmail = autoEmail;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, false);
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public UserModel jSonToObject(String jsonString) throws CustomException {
		UserModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, UserModel.class);
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
