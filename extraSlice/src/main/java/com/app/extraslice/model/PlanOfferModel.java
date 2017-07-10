package com.app.extraslice.model;

import java.io.IOException;
import java.util.List;

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
public class PlanOfferModel {
	String offerName;
	int offerId;
	String offerShortDesc;
	String offerType;
	double offerValue;
	double commitmentValue;
	String commitmentType;
	String applicableTo;
	String monthsExcluded;
	String validFrom;
	String validTill;
	
	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public String getOfferShortDesc() {
		return offerShortDesc;
	}

	public void setOfferShortDesc(String offerShortDesc) {
		this.offerShortDesc = offerShortDesc;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	public double getOfferValue() {
		return offerValue;
	}

	public void setOfferValue(double offerValue) {
		this.offerValue = offerValue;
	}

	public double getCommitmentValue() {
		return commitmentValue;
	}

	public void setCommitmentValue(double commitmentValue) {
		this.commitmentValue = commitmentValue;
	}

	public String getCommitmentType() {
		return commitmentType;
	}

	public void setCommitmentType(String commitmentType) {
		this.commitmentType = commitmentType;
	}

	public String getApplicableTo() {
		return applicableTo;
	}

	public void setApplicableTo(String applicableTo) {
		this.applicableTo = applicableTo;
	}

	public String getMonthsExcluded() {
		return monthsExcluded;
	}

	public void setMonthsExcluded(String monthsExcluded) {
		this.monthsExcluded = monthsExcluded;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTill() {
		return validTill;
	}

	public void setValidTill(String validTill) {
		this.validTill = validTill;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public PlanOfferModel jSonToObject(String jsonString) throws CustomException {
		PlanOfferModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, PlanOfferModel.class);
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
