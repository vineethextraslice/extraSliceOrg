package com.extraslice.walknpay.model;

import com.extraslice.walknpay.bl.CustomException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreModel {
	String name;
	int storeId;
	String addressLine1;
	String addressLine2;
	String city;
	String zip;
	String state;
	String email;
	String phone;
	String logo;
	int dealerId;
	String dealerName;
	String stripeSecretKey, stripePublushKey, paypalEnv, paypalClientId;
	String dlrStripeSecretKey, dlrStripePublushKey, dlrPaypalEnv, dlrPaypalClientId;
	
	Map<String, String> strPayTmAcctMap,dlrPayTmAcctMap;
	boolean active;
	double latitude;
	double longitude;
	double minRechargeAmt;
	String currencyCode;
	String currencySymbol;
	String countryCode;
	
	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getDealerId() {
		return dealerId;
	}

	public void setDealerId(int dealerId) {
		this.dealerId = dealerId;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getStripeSecretKey() {
		return stripeSecretKey;
	}

	public void setStripeSecretKey(String stripeSecretKey) {
		this.stripeSecretKey = stripeSecretKey;
	}

	public String getStripePublushKey() {
		return stripePublushKey;
	}

	public void setStripePublushKey(String stripePublushKey) {
		this.stripePublushKey = stripePublushKey;
	}

	public String getPaypalEnv() {
		return paypalEnv;
	}

	public void setPaypalEnv(String paypalEnv) {
		this.paypalEnv = paypalEnv;
	}

	public String getPaypalClientId() {
		return paypalClientId;
	}

	public void setPaypalClientId(String paypalClientId) {
		this.paypalClientId = paypalClientId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public double getMinRechargeAmt() {
		return minRechargeAmt;
	}

	public void setMinRechargeAmt(double minRechargeAmt) {
		this.minRechargeAmt = minRechargeAmt;
	}

	public String getDlrStripeSecretKey() {
		return dlrStripeSecretKey;
	}

	public void setDlrStripeSecretKey(String dlrStripeSecretKey) {
		this.dlrStripeSecretKey = dlrStripeSecretKey;
	}

	public String getDlrStripePublushKey() {
		return dlrStripePublushKey;
	}

	public void setDlrStripePublushKey(String dlrStripePublushKey) {
		this.dlrStripePublushKey = dlrStripePublushKey;
	}

	public String getDlrPaypalEnv() {
		return dlrPaypalEnv;
	}

	public void setDlrPaypalEnv(String dlrPaypalEnv) {
		this.dlrPaypalEnv = dlrPaypalEnv;
	}

	public String getDlrPaypalClientId() {
		return dlrPaypalClientId;
	}

	public void setDlrPaypalClientId(String dlrPaypalClientId) {
		this.dlrPaypalClientId = dlrPaypalClientId;
	}

	public Map<String, String> getStrPayTmAcctMap() {
		if(strPayTmAcctMap == null){
			strPayTmAcctMap =  new HashMap<String, String>();
			/*strPayTmAcctMap.put("THEME", "merchant");
			strPayTmAcctMap.put("WEBSITE", "worldpressplg");
			strPayTmAcctMap.put("INDUSTRY_TYPE_ID", "Retail");
			strPayTmAcctMap.put("CHANNEL_ID", "WAP");
			strPayTmAcctMap.put("MID", "WorldP64425807474247");
			strPayTmAcctMap.put("CHKSUM_GEN", "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp");
			strPayTmAcctMap.put("CHKSUM_VER", "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/
		}
		return strPayTmAcctMap;
	}

	public void setStrPayTmAcctMap(Map<String, String> strPayTmAcctMap) {
		this.strPayTmAcctMap = strPayTmAcctMap;
	}

	public Map<String, String> getDlrPayTmAcctMap() {
		if(dlrPayTmAcctMap == null){
			dlrPayTmAcctMap =  new HashMap<String, String>();
			/*dlrPayTmAcctMap.put("THEME", "merchant");
			dlrPayTmAcctMap.put("WEBSITE", "worldpressplg");
			dlrPayTmAcctMap.put("INDUSTRY_TYPE_ID", "Retail");
			dlrPayTmAcctMap.put("CHANNEL_ID", "WAP");
			dlrPayTmAcctMap.put("MID", "WorldP64425807474247");
			dlrPayTmAcctMap.put("CHKSUM_GEN", "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp");
			dlrPayTmAcctMap.put("CHKSUM_VER", "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/
		}
		return dlrPayTmAcctMap;
	}

	public void setDlrPayTmAcctMap(Map<String, String> dlrPayTmAcctMap) {
		this.dlrPayTmAcctMap = dlrPayTmAcctMap;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public StoreModel jSonToObject(String jsonString) throws CustomException {
		StoreModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			uModel = mapper.readValue(jsonString, StoreModel.class);
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
